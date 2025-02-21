package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class UserResponseDataModel {

    String  id, name, year, color, pantone_value;

}
