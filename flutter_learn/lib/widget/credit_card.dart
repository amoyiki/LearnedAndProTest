
import 'package:flutter/material.dart';
import 'package:flutter_learn/model/credit_card_model.dart';

class CreditCard extends StatelessWidget {

  final CreditCardViewModel data;

  CreditCard({Key key, this.data}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 180,
      margin: EdgeInsets.fromLTRB(16, 16, 16, 0),
      padding: EdgeInsets.only(left: 20, top: 20),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: this.data.cardColors,
        ),
        borderRadius: BorderRadius.circular(8),
        boxShadow: [
          BoxShadow(
            blurRadius: 6,
            spreadRadius: 4,
            color: Color.fromARGB(20, 0, 0, 0),
          ),
        ]
      ),
      child: Stack(
        children: <Widget>[
          Positioned(
            right: -100,
            bottom: -100,
            child: Image.network(
              this.data.bankLogoUrl,
              width: 250,
              height: 250,
              color: Colors.white10,
            ),
          ),
          Positioned(
            left: 0,
            top: 0,
            right: 0,
            bottom: 0,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                CircleAvatar(
                  radius: 25,
                  backgroundColor: Colors.white,
                  child: Image.network(
                    this.data.bankLogoUrl,
                    width: 36,
                    height: 36,
                    fit: BoxFit.scaleDown,
                  ),
                ),
                Padding(padding: EdgeInsets.only(left: 15)),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Text(
                      this.data.bankName,
                      style: TextStyle(
                        fontSize: 19,
                        fontWeight: FontWeight.bold,
                        color: Colors.white,
                      ),
                    ),
                    Text(
                      this.data.cardType,
                      style: TextStyle(
                        fontSize: 14,
                        color: Color.fromARGB(200, 255, 255, 255),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
          Padding(
            padding: EdgeInsets.only(left: 65, top: 30),
            child: Text(
              this.data.cardNumber,
              style: TextStyle(
                fontSize: 16,
                fontFamily: 'Farrington',
                letterSpacing: 3,
                color: Colors.white,
              ),
            ),
          ),
          Padding(
            padding: EdgeInsets.only(left: 65, top: 15),
            child: Text(
              this.data.validDate,
              style: TextStyle(
                fontSize: 13,
                color: Colors.white,
              ),
            ),
          ),
        ],
      ),
    );
  }
}