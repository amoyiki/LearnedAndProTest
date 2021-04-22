
import 'package:flutter/widgets.dart';
import 'package:flutter_learn/fitness_app/models/meals_list_data.dart';



class MealsListView extends StatefulWidget {
  final AnimationController mainScreenAnimationController;
  final Animation<dynamic> mainScreenAnimation;


  MealsListView({
    Key key,
    this.mainScreenAnimationController, this.mainScreenAnimation
  }) : super(key: key);

  @override
  _MealsListViewState createState() => _MealsListViewState();
}

class _MealsListViewState extends State<MealsListView> 
  with TickerProviderStateMixin {
  
  AnimationController animationController;
  List<MealsListData> mealsListData = MealsListData.tabIconList;

  @override
  void initState() {
    animationController = AnimationController(
      duration: const Duration(milliseconds: 2000),
      vsync: this,
    );
    super.initState();
  }
  Future<bool> getData() async {
    await Future<dynamic>.delayed(const Duration(milliseconds: 50));
    return true;
  }

  @override
  void dispose() {
    animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
       animation: widget.mainScreenAnimationController,
       builder: (BuildContext context, Widget child) {
         return FadeTransition(
           opacity: widget.mainScreenAnimation,
           child: Transform(
             transform: Matrix4.translationValues(0.0, 30* (1.0 - widget.mainScreenAnimation.value), 0.0),
             child: Container(
               height: 216,
               width: double.infinity,
               child: ListView.builder(
                 padding: const EdgeInsets.only(
                   top: 0, bottom: 0, right: 16, left: 16
                 ),
                 itemCount: mealsListData.length,
                 scrollDirection: Axis.horizontal,
                 itemBuilder: (BuildContext context, int index) {
                   final int count = mealsListData.length > 10 ? 10 : mealsListData.length;
                   final Animation<double> animation = Tween<double>(begin: 0.0, end: 1.0).animate(
                     CurvedAnimation(
                       parent: animationController,
                       curve: Interval((1 / count) * index, 1.0, curve: Curves.fastOutSlowIn)
                     )
                   );
                   animationController.forward();
                   return MealsView(
                     mealsListData: mealsListData[index],
                     animation: animation,
                     animationController: animationController,
                   );
                 },
               ),
             ),
           ),
         );
       },
    );
  }
}


class MealsView extends StatelessWidget {
  final MealsListData mealsListData;
  final AnimationController animationController;
  final Animation<dynamic> animation;

  const MealsView({
    Key key,
    this.mealsListData,
    this.animationController,
    this.animation
    }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      
      animation: animationController,
      builder: (BuildContext context, Widget child) {
        return FadeTransition(
          opacity: animation,
          child: Transform(
            transform: Matrix4.translationValues(100*(1.0-animation.value), 0.0, 0.0),
            child: SizedBox(
              width: 130,
              child: Stack(
                children: <Widget>[
                  Padding(
                    padding: const EdgeInsets.only(
                      top: 32, left: 8, right: 8, bottom: 16
                    ),
                    child: Container(
                      decoration: BoxDecoration(
                        boxShadow: <BoxShadow>[
                          BoxShadow(
                            color: 
                          ),

                        ]
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }
}