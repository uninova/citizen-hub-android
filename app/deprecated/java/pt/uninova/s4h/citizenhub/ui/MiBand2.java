package pt.uninova.s4h.citizenhub.ui;

import android.app.Activity;
import android.bluetooth.*;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import javax.crypto.spec.SecretKeySpec;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import datastorage.DatabaseHelperInterface;
import datastorage.MeasurementsContract;

import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_CHARACTERISTIC_NAME;
import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_MEASUREMENT_VALUE;
import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_SOURCE_UUID;
import static datastorage.MeasurementsContract.MeasureEntry.COLUMN_TIMESTAMP;
import static pt.uninova.s4h.citizenhub.ui.Constants.EXTRA_DATA;
import static pt.uninova.s4h.citizenhub.ui.Home.homecontext;

public class MiBand2 extends BluetoothGattCallback {

    public final static UUID UUID_SERVICE_1 = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_STEPS = UUID.fromString("00000007-0000-3512-2118-0009af100700");

    public final static UUID UUID_SERVICE_ALERT = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_ALERT = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_SERVICE_AUTH = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_AUTH = UUID.fromString("00000009-0000-3512-2118-0009af100700");
    public final static UUID UUID_DESCRIPTOR_AUTH = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_SERVICE_HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_HEART_RATE_CONTROL = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC_HEART_RATE_DATA = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");

    private final Map<String, BluetoothGattCharacteristic> characteristics;
    private final SecureRandom keyGenerator;
    private BluetoothGatt gatt;
    private boolean ready;
    private byte[] key;

    public MiBand2() {
        this.ready = false;
        this.characteristics = new HashMap<>();
        this.keyGenerator = new SecureRandom();
        this.key = new byte[16];
    }

    private void heartrate() {
        BluetoothGattCharacteristic hr = characteristics.get("heartrate_data");
        gatt.setCharacteristicNotification(hr, true);

        BluetoothGattDescriptor descriptor = hr.getDescriptor(UUID_DESCRIPTOR_AUTH);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

    private void steps() {
        Log.d("STEPPING", "START");

        final BluetoothGattCharacteristic st = characteristics.get("steps");

        final Handler h = new Handler(Looper.getMainLooper());

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                gatt.readCharacteristic(st);
            }
        }, 10000);
    }

    private void authenticate() {
        BluetoothGattCharacteristic auth = characteristics.get("auth");
        boolean value = gatt.setCharacteristicNotification(auth, true);

        BluetoothGattDescriptor descriptor = auth.getDescriptor(UUID_DESCRIPTOR_AUTH);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        gatt.writeDescriptor(descriptor);
    }

    private void sendNewKey() {
        BluetoothGattCharacteristic auth = characteristics.get("auth");
        byte[] message = new byte[18];

        keyGenerator.nextBytes(key);

        message[0] = 0x01;
        message[1] = 0x08;

        for (int i = 2; i < message.length; i++) {
            message[i] = key[i - 2];
        }

        auth.setValue(message);
        gatt.writeCharacteristic(auth);
    }

    private void requestRandomKey() {
        BluetoothGattCharacteristic auth = characteristics.get("auth");
        byte[] value = new byte[]{0x01, 0x08};

        Log.i("HEXA", Arrays.toString(value));
        auth.setValue(value);
        gatt.writeCharacteristic(auth);
    }

    private void initialize() {
        BluetoothGattService service = gatt.getService(UUID_SERVICE_AUTH);
        characteristics.put("auth", service.getCharacteristic(UUID_CHARACTERISTIC_AUTH));

        service = gatt.getService(UUID_SERVICE_ALERT);
        characteristics.put("alert", service.getCharacteristic(UUID_CHARACTERISTIC_ALERT));

        service = gatt.getService(UUID_SERVICE_HEART_RATE);
        characteristics.put("heartrate_data", service.getCharacteristic(UUID_CHARACTERISTIC_HEART_RATE_DATA));
        characteristics.put("heartrate_control", service.getCharacteristic(UUID_CHARACTERISTIC_HEART_RATE_CONTROL));

        service = gatt.getService(UUID_SERVICE_1);
        characteristics.put("steps", service.getCharacteristic(UUID_CHARACTERISTIC_STEPS));

        authenticate();

        ready = true;
    }

    @Override
    public void onCharacteristicChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        Log.i("MiBand2", "onCharacteristicChanged " + characteristic.getUuid().toString() + " " + Arrays.toString(characteristic.getValue()));

        final byte[] value = characteristic.getValue();

        if (characteristic.getUuid().equals(UUID_CHARACTERISTIC_AUTH)) {
            if (value[0] == 0x10 && value[1] == 0x01 && value[2] == 0x01) {
                characteristic.setValue(new byte[]{0x02, 0x08});
                gatt.writeCharacteristic(characteristic);
            } else if (value[0] == 0x10 && value[1] == 0x02 && value[2] == 0x01) {
                try {
                    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
                    SecretKeySpec secret = new SecretKeySpec(key, "AES");

                    cipher.init(Cipher.ENCRYPT_MODE, secret);

                    byte[] handshake = cipher.doFinal(Arrays.copyOfRange(value, 3, 19));
                    byte[] message = new byte[18];

                    message[0] = 0x03;
                    message[1] = 0x08;

                    for (int i = 0; i < handshake.length; i++) {
                        message[i + 2] = handshake[i];
                    }
                    characteristic.setValue(message);
                    gatt.writeCharacteristic(characteristic);


                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            } else if (value[0] == 0x10 && value[1] == 0x03 && value[2] == 0x01) {
                heartrate();
                steps();
            }
        } else if (characteristic.getUuid().equals(UUID_CHARACTERISTIC_HEART_RATE_DATA)) {
            int heartrate = characteristic.getValue()[1];
            saveData(characteristic.getUuid(), heartrate, "HeartRate");
        }
    }

    @Override
    public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);

        Log.d("MiBand2", "onCharacteristicRead " + characteristic.getUuid().toString() + " " + Arrays.toString(characteristic.getValue()) + " " + status);

        final byte[] value = characteristic.getValue();

        if (characteristic.getUuid().equals(UUID_CHARACTERISTIC_STEPS)) {
            ByteBuffer val = ByteBuffer.wrap(value);
            val.order(ByteOrder.LITTLE_ENDIAN);

            final int value1 = val.getInt(1); //steps
            final int value2 = val.getInt(5);//distance
            final int value3 = val.getInt(9);   //kcal

            saveData(characteristic.getUuid(), value1, "Steps");
            saveData(characteristic.getUuid(), value2, "Distance");
            saveData(characteristic.getUuid(), value3, "Calories");

            final Handler h = new Handler(Looper.getMainLooper());

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gatt.readCharacteristic(characteristic);
                }
            }, 10000);

        }
    }

    public void saveData(UUID charUUID, int value, String characteristicName) {
        SQLiteOpenHelper SQLiteOpenHelper = new DatabaseHelperInterface(homecontext);
        SQLiteDatabase db = SQLiteOpenHelper.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        String timestamp = cal.getTime().toString();
        String values = String.valueOf(value);
        String name = characteristicName;
        String uuid = charUUID.toString();

        ContentValues Cvalues = new ContentValues();
        Cvalues.put(COLUMN_TIMESTAMP, timestamp);
        Cvalues.put(COLUMN_MEASUREMENT_VALUE, values);
        Cvalues.put(COLUMN_CHARACTERISTIC_NAME, name);
        Cvalues.put(COLUMN_SOURCE_UUID, uuid);

        db.insertWithOnConflict("measurements", null, Cvalues, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d("TABLEWTF", String.valueOf(Home.SQLiteOpenHelper.getReadableDatabase().query(MeasurementsContract.MeasureEntry.TABLE_NAME, null, null, null, null, null, null)));
    }

    @Override
    public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        Log.i("MiBand2", "onCharacteristicWrite " + characteristic.getUuid().toString() + " " + Arrays.toString(characteristic.getValue()) + " " + status);

        if (characteristic.getUuid().equals(UUID_CHARACTERISTIC_HEART_RATE_CONTROL)) {
            byte[] value = characteristic.getValue();

                if (value[0] == 0x15 && value[1] == 0x02 && value[2] == 0x00) {
                    characteristic.setValue(new byte[]{0x15, 0x01, 0x00});
                    gatt.writeCharacteristic(characteristic);
                } else if (value[0] == 0x15 && value[1] == 0x01 && value[2] == 0x00) {
                    characteristic.setValue(new byte[]{0x15, 0x01, 0x01});
                    gatt.writeCharacteristic(characteristic);
                } else if (value[0] == 0x15 && value[1] == 0x01 && value[2] == 0x01) {
                    Log.i("BOO", "YAAA!");
                    final Handler h = new Handler(Looper.getMainLooper());

                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            characteristic.setValue(new byte[]{0x16});
                            gatt.writeCharacteristic(characteristic);

                            h.postDelayed(this, 10000);
                        }
                    }, 10000);
                }
        }
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        Log.i("MiBand2", "onConnectionStateChange " + status + " " + newState);

        if (newState == BluetoothProfile.STATE_CONNECTED) {
            if (!ready) {
                this.gatt = gatt;

                this.gatt.discoverServices();
            }
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);

        if (descriptor.getCharacteristic().getUuid().equals(UUID_CHARACTERISTIC_AUTH)) {
            sendNewKey();
        } else if (descriptor.getCharacteristic().getUuid().equals(UUID_CHARACTERISTIC_HEART_RATE_DATA)) {
            BluetoothGattCharacteristic hrctl = characteristics.get("heartrate_control");
            hrctl.setValue(new byte[]{0x15, 0x02, 0x00});
            gatt.writeCharacteristic(hrctl);
        }
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        super.onReliableWriteCompleted(gatt, status);
        Log.i("MiBand2", "onReliableWriteCompleted " + status);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        Log.i("MiBand2", "onServicesDiscovered " + status);

        List<BluetoothGattService> services = gatt.getServices();

        for (BluetoothGattService i : services) {
            Log.i("SERVICES", i.getUuid().toString());
        }

        BluetoothGattService service = gatt.getService(UUID_SERVICE_HEART_RATE);

        Log.i("SERVICE", service.getUuid().toString());

        for (BluetoothGattCharacteristic i : service.getCharacteristics())
            Log.i("CHARACTERISTIC", i.getUuid().toString());

        initialize();
    }
}
