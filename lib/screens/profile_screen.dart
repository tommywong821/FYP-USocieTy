import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/profile_screen_arguments.dart';
import '../services/aad_oauth_service.dart';
import '../services/storage_service.dart';
import '../model/styles.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({
    Key? key,
  }) : super(key: key);

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  final AadOAuthService _aadOAuthService = AadOAuthService();
  final StorageService _storageService = StorageService();

  @override
  Widget build(BuildContext context) {
    final args =
        ModalRoute.of(context)!.settings.arguments as ProfileScreenArguments;

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.white,
        centerTitle: true,
        title: Text(
          "My Profile",
          style: Styles.carouselTitle,
        ),
      ),
      body: SingleChildScrollView(
        physics: AlwaysScrollableScrollPhysics(),
        child: Padding(
          padding: const EdgeInsets.only(top: 10, bottom: 10),
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                SizedBox(
                  height: 115,
                  width: 115,
                  child: Stack(
                    fit: StackFit.expand,
                    clipBehavior: Clip.none,
                    children: [
                      CircleAvatar(
                        backgroundImage: AssetImage("assets/images/avatar.png"),
                      ),
                      Positioned(
                        right: -16,
                        bottom: 0,
                        child: SizedBox(
                          height: 46,
                          width: 46,
                        ),
                      )
                    ],
                  ),
                ),
                Padding(
                    padding: EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                    child: TextButton(
                      style: TextButton.styleFrom(
                        side: BorderSide(color: Styles.primaryColor),
                        padding: EdgeInsets.all(15),
                        shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(20)),
                        backgroundColor: Colors.white,
                      ),
                      onPressed: () {},
                      child: Row(children: [
                        Icon(Icons.person_outlined, color: Styles.primaryColor),
                        SizedBox(width: 20),
                        Expanded(
                            child: Text("Name: ${args.fullname}",
                                style: TextStyle(color: Styles.primaryColor))),
                      ]),
                    )),
                Padding(
                    padding: EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                    child: TextButton(
                      style: TextButton.styleFrom(
                        side: BorderSide(color: Styles.primaryColor),
                        padding: EdgeInsets.all(15),
                        shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(20)),
                        backgroundColor: Colors.white,
                      ),
                      onPressed: () {},
                      child: Row(children: [
                        Icon(
                          Icons.email_outlined,
                          color: Styles.primaryColor,
                        ),
                        SizedBox(width: 20),
                        Expanded(
                            child: Text("Nickname: ${args.nickname}",
                                style: TextStyle(color: Styles.primaryColor))),
                      ]),
                    )),
                Padding(
                    padding: EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                    child: TextButton(
                      style: TextButton.styleFrom(
                        side: BorderSide(color: Styles.primaryColor),
                        padding: EdgeInsets.all(15),
                        shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(20)),
                        backgroundColor: Colors.white,
                      ),
                      onPressed: () {},
                      child: Row(children: [
                        Icon(
                          Icons.email_outlined,
                          color: Styles.primaryColor,
                        ),
                        SizedBox(width: 20),
                        Expanded(
                            child: Text("ITSC account: ${args.email}",
                                style: TextStyle(color: Styles.primaryColor))),
                      ]),
                    )),
                Padding(
                    padding: EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                    child: TextButton(
                      style: TextButton.styleFrom(
                        side: BorderSide(color: Styles.primaryColor),
                        padding: EdgeInsets.all(15),
                        shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(20)),
                        backgroundColor: Colors.white,
                      ),
                      onPressed: () {},
                      child: Row(children: [
                        Icon(Icons.people_outline, color: Styles.primaryColor),
                        SizedBox(width: 20),
                        Expanded(
                            child: Text(
                                "Joined Society: ${args.enrolledSocieties}",
                                style: TextStyle(color: Styles.primaryColor))),
                      ]),
                    )),
                Padding(
                    padding: EdgeInsets.only(top: 10),
                    child: Container(
                      width: 140,
                      child: TextButton(
                        style: TextButton.styleFrom(
                          side: BorderSide(color: Styles.primaryColor),
                          padding: EdgeInsets.all(10),
                          shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(35)),
                          backgroundColor: Colors.white,
                        ),
                        //notice
                        onPressed: () {
                          logout();
                        },
                        child: Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Icon(Icons.exit_to_app_outlined,
                                  color: Styles.primaryColor),
                              SizedBox(width: 15),
                              Text("Log Out",
                                  style: TextStyle(color: Colors.black)),
                            ]),
                      ),
                    )),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Future<void> logout() async {
    // route to profile page
    await _aadOAuthService.logout();
    await _storageService.deleteAllSecureData();

    //route to welcome page when logout
    routeToWelcomePage();
  }

  void routeToWelcomePage() {
    Navigator.pushNamedAndRemoveUntil(context, '/', ModalRoute.withName('/'));
  }
}
