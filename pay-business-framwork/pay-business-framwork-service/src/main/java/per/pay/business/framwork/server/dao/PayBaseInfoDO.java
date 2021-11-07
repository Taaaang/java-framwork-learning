package per.pay.business.framwork.server.dao;

import lombok.Data;

@Data
public class PayBaseInfoDO {

  private long payId;

  private String payType;

  private String supplier;
}
