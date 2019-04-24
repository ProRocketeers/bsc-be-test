package com.prorocketeers.bsc.model;

/**
 * @author - zazodan
 */
public enum CurrencyCode {

  CZK("CZK", 21.78),
  EUR("EUR", 0.89),
  HKD("HKD", 0.13),
  RMB("RMB", 0.15),
  USD("USD", 1D);

  private String currencyCode;
  private Double rateToDollar;

  CurrencyCode(String currencyCode, Double rateToDollar) {
    this.currencyCode = currencyCode;
    this.rateToDollar = rateToDollar;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public Double getRateToDollar() {
    return rateToDollar;
  }

}