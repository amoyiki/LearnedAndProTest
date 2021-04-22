import 'package:flutter/material.dart';
import 'package:flutter_learn/model/pet_card_model.dart';

class PetCard extends StatelessWidget {
  final PetCardViewModel data;

  const PetCard({Key key, this.data}) : super(key: key);


  Widget renderCover() {
    return Stack(
      fit: StackFit.passthrough,
      children: <Widget>[
        ClipRRect(
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(8),
            topRight: Radius.circular(8)
          ),
          child: Image.network(
            data.coverUrl,
            height: 200,
            fit: BoxFit.fitWidth,
          ),
        ),
        Positioned(
          left: 0,
          top: 100,
          right: 0,
          bottom: 0,
          child: Container(
            decoration: BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topCenter,
                end: Alignment.bottomCenter,
                colors: [
                  Color.fromARGB(0, 0, 0, 0),
                  Color.fromARGB(80, 0, 0, 0),
                ]
              )
            ),
          ),
        )
      ],
    );
  }

  Widget renderUserInfo() {
    return Container(
      margin: EdgeInsets.only(top: 16),
      padding: EdgeInsets.symmetric(horizontal: 16),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          Row(
            children: <Widget>[
              CircleAvatar(
                radius: 20,
                backgroundColor: Color(0xFFCCCCCC),
                backgroundImage: NetworkImage(data.userImgUrl),
              ),
              Padding(padding: EdgeInsets.only(left: 8),),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text(
                    data.userName,
                    style: TextStyle(
                      fontSize: 15,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF3333333),
                    ),
                  ),
                  Padding(padding: EdgeInsets.only(top: 2)),
                  Text(
                    data.description,
                    style: TextStyle(
                      fontSize: 12,
                      color: Color(0xFF9999999),
                    ),
                  ),
                ],
              ),
            ],
          ),
          Text(
            data.publishTime,
            style: TextStyle(
              fontSize: 13,
              color: Color(0xFF9999999),
            ),
          ),
        ],
      ),
    );
  }

  Widget renderPublishContent() {
    return Container(
        margin: EdgeInsets.only(top: 16),
        padding: EdgeInsets.symmetric(horizontal: 16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Container(
              margin: EdgeInsets.only(bottom: 14),
              padding: EdgeInsets.symmetric(horizontal: 8, vertical: 2),
              decoration: BoxDecoration(
                color: Color(0xFFFFC600),
                borderRadius: BorderRadius.only(
                  topRight: Radius.circular(8),
                  bottomLeft: Radius.circular(8),
                  bottomRight: Radius.circular(8),
                  
                ),
              ),
              child: Text(
                '# ${data.topic}',
                style: TextStyle(
                  fontSize: 12,
                  color: Colors.white,
                ),
              ),
            ),
            Text(
              data.publishContent,
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
              style: TextStyle(
                fontSize: 15,
                fontWeight: FontWeight.bold,
                color: Color(0xFF3333333),
              ),
            ),

          ],
        ),
    );
  }

  Widget renderInterationArea() {
    return Container(
      margin: EdgeInsets.symmetric(vertical: 16),
      padding: EdgeInsets.symmetric(horizontal: 16),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          Row(
            children: <Widget>[
              Icon(
                Icons.message,
                size: 16,
                color: Color(0xFF9999999),
              ),
              Padding(padding: EdgeInsets.only(left: 6)),
              Text(
                data.replies.toString(),
                style: TextStyle(
                  fontSize: 15,
                  color: Color(0xFF9999999),
                ),
              ),
            ],
          ),
          Row(
            children: <Widget>[
              Icon(Icons.favorite,
                size: 16,
                color: Color(0XFFFFC600),
              ),
              Padding(padding: EdgeInsets.only(left: 6)),
              Text(
                data.likes.toString(),
                style: TextStyle(
                  fontSize: 15,
                  color: Color(0xFF9999999),
                ),
              ),
            ],
          ),
          Row(
            children: <Widget>[
              Icon(Icons.share,
                size: 16,
                color: Color(0xFF9999999),
              ),
              Padding(padding: EdgeInsets.only(left: 6)),
              Text(
                data.shares.toString(),
                style: TextStyle(
                  fontSize: 15,
                  color: Color(0xFF9999999),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }


  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [
          BoxShadow(
            blurRadius: 6,
            spreadRadius: 4,
            color: Color.fromARGB(20, 0, 0, 0),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          this.renderCover(),
          this.renderUserInfo(),
          this.renderPublishContent(),
          this.renderInterationArea(),
        ],
      ),
    );
  }
}