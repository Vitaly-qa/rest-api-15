package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserResponseDataModel {
    private InnerDataModel data;

    @Data
    public static class InnerDataModel {
        private String id;
        private String name;
        private String year;
        private String color;
        @JsonProperty("pantone_value")
        private String pantoneValue;
    }
}
