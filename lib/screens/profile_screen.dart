import 'dart:io';
import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/main.dart';
import 'package:ngok3fyp_frontend_flutter/model/profile_screen_arguments.dart';
import '../constants.dart';
import '../services/aad_oauth_service.dart';
import '../services/api_service.dart';
import '../services/storage_service.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:image_picker/image_picker.dart';
import 'package:firebase_storage/firebase_storage.dart';

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
  String profilePicLink = "";
  String imageName = "";

  Future _loadImage() async {
    try {
      final storedRefreshToken =
          await _storageService.readSecureData(ACCESS_TOKEN_KEY);
      final profile =
          await ApiService().getUserDetails(storedRefreshToken.toString());
      String tempImageName = profile.itsc;
      setState(() {
        imageName = tempImageName;
      });
      var ref = FirebaseStorage.instance.ref().child('profileImage/$imageName');
      String url = (await ref.getDownloadURL()).toString();
      setState(() {
        profilePicLink = url;
      });
    } on Exception catch (e) {
      print(e);
    }
  }

  Future _pickImage(ImageSource source) async {
    try {
      final image = await ImagePicker().pickImage(source: source);
      if (image == null) return;
      Reference ref =
          FirebaseStorage.instance.ref().child('profileImage/$imageName');
      await ref.putFile(File(image.path));
      ref.getDownloadURL().then((value) async {
        setState(() {
          profilePicLink = value;
          Navigator.of(context).pop();
        });
        print(value);
      });
    } on Exception catch (e) {
      print(e);
      Navigator.of(context).pop();
    }
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    _loadImage();
  }

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
      body: SafeArea(
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
                    InkWell(
                      onTap: () {
                        showModalBottomSheet(
                          context: context,
                          builder: ((builder) => bottomSheet()),
                        );
                      },
                      child: profilePicLink == ""
                          ? CircleAvatar(
                              backgroundImage:
                                  AssetImage("assets/images/avatar.png"),
                            )
                          : CircleAvatar(
                              backgroundImage: NetworkImage(profilePicLink),
                            ),
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
                          child: Text("Joined Society: ${args.enrolledSocieties}",
                              style: TextStyle(color: Styles.primaryColor))),
                    ]),
                  )),
              Padding(
                  padding: EdgeInsets.symmetric(horizontal: 140, vertical: 10),
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
                    child: Row(children: [
                      Icon(Icons.exit_to_app_outlined,
                          color: Styles.primaryColor),
                      SizedBox(width: 15),
                      Expanded(
                          child: Text("LOG OUT",
                              style: TextStyle(color: Colors.black))),
                    ]),
                  )),
            ],
          ),
        ),
      ),
    );
  }

  Widget bottomSheet() {
    return Container(
      height: 100.0,
      width: MediaQuery.of(context).size.width,
      margin: EdgeInsets.symmetric(
        horizontal: 20,
        vertical: 20,
      ),
      child: Column(
        children: <Widget>[
          Text(
            "Choose Profile photo",
            style: TextStyle(fontSize: 20.0, color: Styles.primaryColor),
          ),
          SizedBox(
            height: 20,
          ),
          Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: <Widget>[
                TextButton(
                  child: Icon(
                    Icons.camera_alt_outlined,
                    color: Styles.primaryColor,
                  ),
                  onPressed: () {
                    _pickImage(ImageSource.camera);
                  },
                ),
                TextButton(
                  child: Icon(Icons.image_outlined, color: Styles.primaryColor),
                  onPressed: () {
                    _pickImage(ImageSource.gallery);
                  },
                ),
              ])
        ],
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
