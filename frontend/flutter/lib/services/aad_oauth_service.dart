import 'package:aad_oauth/aad_oauth.dart';
import 'package:aad_oauth/model/config.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';

import '../main.dart';

class AadOAuthService {
  static final Config config = Config(
      tenant: dotenv.env['TENANT']!,
      clientId: dotenv.env['CLIENTID']!,
      redirectUri: dotenv.env['REDIRECTURI']!,
      scope: dotenv.env['SCOPE']!,
      clientSecret: dotenv.env['CLIENTSECRET']!,
      navigatorKey: navigatorKey);

  final AadOAuth oAuth = AadOAuth(config);

  Future<void> login() async {
    await oAuth.login();
  }

  Future<String?> getAccessToken() async {
    return await oAuth.getAccessToken();
  }

  Future<void> logout() async {
    await oAuth.logout();
  }
}
