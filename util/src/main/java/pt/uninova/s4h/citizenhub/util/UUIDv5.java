package pt.uninova.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class UUIDv5 {

    private final UUID namespace;
    private final byte[] namespaceBytes;
    private final MessageDigest messageDigest;

    public UUIDv5(String name) throws NoSuchAlgorithmException {
        this((new UUIDv5(new UUID(0, 0))).getUUID(name));
    }

    public UUIDv5(UUID namespace) throws NoSuchAlgorithmException {
        this.namespace = namespace;
        namespaceBytes =
                ByteBuffer.allocate(16)
                        .order(ByteOrder.BIG_ENDIAN)
                        .putLong(namespace.getMostSignificantBits())
                        .putLong(namespace.getLeastSignificantBits())
                        .array();

        this.messageDigest = MessageDigest.getInstance("SHA-1");
    }

    public UUID getNamespace() {
        return namespace;
    }

    public UUID getUUID(String name) {
        final byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);

        final byte[] in =
                ByteBuffer.allocate(namespaceBytes.length + nameBytes.length)
                        .order(ByteOrder.BIG_ENDIAN)
                        .put(namespaceBytes)
                        .put(nameBytes)
                        .array();

        final byte[] digest = messageDigest.digest(in);

        digest[6] &= 0x0f;
        digest[6] |= 0x50;
        digest[8] &= 0x3f;
        digest[8] |= 0x80;

        final ByteBuffer out = ByteBuffer.wrap(digest);

        return new UUID(out.getLong(), out.getLong());
    }
}
