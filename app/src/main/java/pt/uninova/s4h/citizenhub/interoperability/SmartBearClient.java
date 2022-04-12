package pt.uninova.s4h.citizenhub.interoperability;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pt.uninova.util.messaging.Observer;

public class SmartBearClient implements Uploader<DailyPostureReport> {

    private final OkHttpClient httpClient;
    private final String url;
    private final String apiKey;

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public SmartBearClient(String url, String apiKey) {
        this.url = url;
        this.apiKey = apiKey;

        httpClient = new OkHttpClient();
    }

    private String parseHourlyAggregate(int[] values) {
        final StringBuilder builder = new StringBuilder();

        builder.append(values[0]);

        for (int i = 1; i < values.length; i++) {
            builder.append(" ").append(values[i]);
        }

        return builder.toString();
    }

    @Override
    public void upload(DailyPostureReport content, Observer<Response> observer) {
        final String jsonPlaceholder = "{\"resourceType\":\"Bundle\",\"type\":\"transaction\",\"entry\":[{\"request\":{\"method\":\"POST\",\"url\":\"Observation\"},\"fullUrl\":\"urn:uuid:%6$s\",\"resource\":{\"resourceType\":\"Observation\",\"status\":\"final\",\"code\":{\"coding\":[{\"system\":\"http://snomed.info/sct\",\"code\":\"51093005\",\"display\":\"Bodyposturenormal\"}],\"text\":\"Theamountoftimespentinanormalposture\"},\"category\":[{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/observation-category\",\"code\":\"activity\",\"display\":\"Activity\"}]}],\"subject\":{\"reference\":\"Patient/%1$s\"},\"effectivePeriod\":{\"start\":\"%2$s\",\"end\":\"%3$s\"},\"valueSampledData\":{\"origin\":{\"value\":0,\"unit\":\"second\",\"system\":\"http://unitsofmeasure.org\",\"code\":\"s\"},\"period\":3600000,\"lowerLimit\":0,\"upperLimit\":3600,\"dimensions\":1,\"data\":\"%4$s\"}}},{\"request\":{\"method\":\"POST\",\"url\":\"Observation\"},\"fullUrl\":\"urn:uuid:%7$s\",\"resource\":{\"resourceType\":\"Observation\",\"status\":\"final\",\"code\":{\"coding\":[{\"system\":\"http://snomed.info/sct\",\"code\":\"249858009\",\"display\":\"Poorposture\"}],\"text\":\"Theamountoftimespentinapoorposture\"},\"category\":[{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/observation-category\",\"code\":\"activity\",\"display\":\"Activity\"}]}],\"subject\":{\"reference\":\"Patient/%1$s\"},\"effectivePeriod\":{\"start\":\"%2$s\",\"end\":\"%3$s\"},\"valueSampledData\":{\"origin\":{\"value\":0,\"unit\":\"second\",\"system\":\"http://unitsofmeasure.org\",\"code\":\"s\"},\"period\":3600000,\"lowerLimit\":0,\"upperLimit\":3600,\"dimensions\":1,\"data\":\"%5$s\"}}},{\"request\":{\"method\":\"POST\",\"url\":\"DiagnosticReport\"},\"resource\":{\"resourceType\":\"DiagnosticReport\",\"status\":\"final\",\"text\":{\"status\":\"empty\"},\"code\":{\"coding\":[{\"system\":\"http://snomed.info/sct\",\"code\":\"298341003\",\"display\":\"Findingofbodyposture\"}],\"text\":\"Dailyreportaboutthepostureofapatient\"},\"subject\":{\"reference\":\"Patient/%1$s\"},\"effectivePeriod\":{\"start\":\"%2$s\",\"end\":\"%3$s\"},\"result\":[{\"reference\":\"urn:uuid:%6$s\"},{\"reference\":\"urn:uuid:%7$s\"}]}}]}";
        final String body = String.format(jsonPlaceholder, content.getPatientId(), content.getStartTime().toString(), content.getEndTime().toString(), parseHourlyAggregate(content.getHourlyGoodPosture()), parseHourlyAggregate(content.getHourlyBadPosture()), UUID.randomUUID().toString(), UUID.randomUUID().toString());

        final RequestBody reqBody = RequestBody.create(body, MediaType.get("application/json"));
        final Request request = new Request.Builder()
                .url(url)
                .header("apikey", apiKey)
                .post(reqBody)
                .build();

        executorService.execute(() -> {
            try (final Response response = httpClient.newCall(request).execute()) {
                observer.observe(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void upload(Collection<DailyPostureReport> content, Observer<Collection<Response>> observer) {
        final Collection<Response> responses = new ArrayList<>(content.size());

        for (DailyPostureReport i : content) {
            upload(i, value -> {
                synchronized (responses) {
                    responses.add(value);

                    if (responses.size() == content.size()) {
                        observer.observe(responses);
                    }
                }
            });
        }
    }
}
