package ngok3.fyp.backend.controller.authentication.model

class MockAuthRepository {

    /* {
         "itsc": "valid itsc",
         "name": "valid tester",
         "mail": "validItsc@connect.ust.hk",
         "role": "",
         "exp": 2432977350
     }*/
    val validUserCookieToken: String =
        "eyJhbGciOiJIUzI1NiJ9.eyJpdHNjIjoidmFsaWQgaXRzYyIsIm5hbWUiOiJ2YWxpZCB0ZXN0ZXIiLCJtYWlsIjoidmFsaWRJdHNjQGNvbm5lY3QudXN0LmhrIiwicm9sZSI6W10sImV4cCI6MTcxMzUwODM2OH0.9OZrGEe8ZFvVwUmC4G2OqC5OTUHVQ8iEpOaFLCOcatk"
    val validUserItsc: String = "valid itsc"
    val validUserNickname: String = "valid tester"
    val validUserMail: String = "validItsc@connect.ust.hk"

    /*    {
            "itsc": "invalid itsc",
            "name": "invalid tester",
            "mail": "invalidItsc@connect.ust.hk",
            "role": "",
            "exp": 2432977478
        }*/
    val invalidUserCookieToken: String =
        "eyJhbGciOiJIUzI1NiJ9.eyJpdHNjIjoiaW52YWxpZCBpdHNjIiwibmFtZSI6ImludmFsaWQgdGVzdGVyIiwibWFpbCI6ImludmFsaWRJdHNjQGNvbm5lY3QudXN0LmhrIiwicm9sZSI6W10sImV4cCI6MTcxMzUwODQ2N30.u1lRpT4mEn90rqH700W9n4zcuKFfc7V9FIu7IEytCyM"
    val invalidUserItsc: String = "invalid itsc"

    val testSocietyName: String = "test society"

}