import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/profile_screen_arguments.dart';
import 'package:intl/intl.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import '../../../constants.dart';
import '../../../services/aad_oauth_service.dart';
import '../../../services/api_service.dart';
import '../../../services/storage_service.dart';
import 'package:firebase_storage/firebase_storage.dart';

class AppBarWidget extends StatefulWidget {
  const AppBarWidget({Key? key}) : super(key: key);

  @override
  State<AppBarWidget> createState() => _AppBarWidgetState();
}

class _AppBarWidgetState extends State<AppBarWidget> {
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
    @override
  void initState() {
    super.initState();
    _loadImage();
  }
  @override
  Widget build(BuildContext context) {

    var args =
        ModalRoute.of(context)!.settings.arguments as ProfileScreenArguments;
    var now = new DateTime.now().toLocal();
    var formatter = new DateFormat('EEEE, MMMM d');
    String formattedDate = formatter.format(now);
    return Container(
      child: Column(
        children: [
          Padding(
            padding: EdgeInsets.only(top: 10, left: 15),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                    child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      formattedDate,
                      style: Styles.appBarDate,
                    ),
                    SizedBox(
                      height: 3,
                    ),
                    Text("Hi! " + args.fullname, style: Styles.appBarName),
                  ],
                )),
                    InkWell(
                      child: profilePicLink == ""
                          ? CircleAvatar(
                              backgroundImage:
                                  AssetImage("assets/images/avatar.png"),
                            )
                          : CircleAvatar(
                              backgroundImage: NetworkImage(profilePicLink),
                            ),
                    ),
              ],
            ),
          ),
          SizedBox(
            height: 10,
          ),
          // SizedBox(
          //   child: Divider(
          //     indent: 20,
          //     endIndent: 20,
          //     thickness: 0.5,
          //     color: Colors.grey.withOpacity(0.5),
          //   ),
          // )
        ],
      ),
    );
  }
}
