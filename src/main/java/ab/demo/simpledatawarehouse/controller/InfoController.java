package ab.demo.simpledatawarehouse.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @Value("${application.name}")
    private String applicationName;

    @Value("${build.version}")
    private String buildVersion;

    @Value("${build.timestamp}")
    private String buildTimestamp;

    @GetMapping(value = "/info", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[Application info]");
        sb.append("\nApplication name : ");
        sb.append(applicationName);
        sb.append("\nBuild version    : ");
        sb.append(buildVersion);
        sb.append("\nBuild timestamp  : ");
        sb.append(buildTimestamp);
        return sb.toString();
    }

}
