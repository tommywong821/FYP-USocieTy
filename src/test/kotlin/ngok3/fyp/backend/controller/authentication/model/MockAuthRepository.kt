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
        "eyJhbGciOiJIUzI1NiJ9.eyJpdHNjIjoidmFsaWQgaXRzYyIsIm5hbWUiOiJ2YWxpZCB0ZXN0ZXIiLCJtYWlsIjoidmFsaWRJdHNjQGNvbm5lY3QudXN0LmhrIiwicm9sZSI6W10sImV4cCI6MTY4MTcxMTIxMn0.QbzJebOXhMXGiPfM3dkEQHG-mcDMXrQ2b5OVywgKv2k"
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
        "eyJhbGciOiJIUzI1NiJ9.eyJpdHNjIjoiaW52YWxpZCBpdHNjIiwibmFtZSI6ImludmFsaWQgdGVzdGVyIiwibWFpbCI6ImludmFsaWRJdHNjQGNvbm5lY3QudXN0LmhrIiwicm9sZSI6W10sImV4cCI6MTY4MTcxMTE0OX0.xZwtCQFNSqh2EC2SwYYeqrOOU9AYh689e1ya0cFTgLU"
    val invalidUserItsc: String = "invalid itsc"

    val testSocietyName: String = "test society"

}