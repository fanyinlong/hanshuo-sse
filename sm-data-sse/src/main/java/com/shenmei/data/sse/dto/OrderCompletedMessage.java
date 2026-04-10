
package com.shenmei.data.sse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ruleId;

    private Long executeRecordId;

    private Long totalOrderCount;

    private String executeUser;

    private Long timestamp;
}
