import 'package:flutter/material.dart';

class CreditCardViewModel {
  final String bankName;
  final String bankLogoUrl;
  final String cardType;
  final String cardNumber;
  final List<Color> cardColors;
  final String validDate;

  const CreditCardViewModel({
    this.bankName, 
    this.bankLogoUrl,
    this.cardType,
    this.cardNumber, 
    this.cardColors,
    this.validDate
  });
}
