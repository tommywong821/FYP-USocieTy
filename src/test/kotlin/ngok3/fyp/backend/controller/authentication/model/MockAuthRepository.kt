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
        "eyJhbGciOiJIUzI1NiJ9.eyJpdHNjIjoidmFsaWQgaXRzYyIsIm5hbWUiOiJ2YWxpZCB0ZXN0ZXIiLCJtYWlsIjoidmFsaWRJdHNjQGNvbm5lY3QudXN0LmhrIiwicm9sZSI6IiIsImV4cCI6MjQzMjk3NzM1MH0.kbUz6xtY0n1WEEZEb5WTzWp206Lp6Ze_3Vt9rAcz9J4"
    val validUserItsc: String = "valid itsc"

    /*    {
            "itsc": "invalid itsc",
            "name": "invalid tester",
            "mail": "invalidItsc@connect.ust.hk",
            "role": "",
            "exp": 2432977478
        }*/
    val invalidUserCookieToken: String =
        "eyJhbGciOiJIUzI1NiJ9.eyJpdHNjIjoiaW52YWxpZCBpdHNjIiwibmFtZSI6ImludmFsaWQgdGVzdGVyIiwibWFpbCI6ImludmFsaWRJdHNjQGNvbm5lY3QudXN0LmhrIiwicm9sZSI6IiIsImV4cCI6MjQzMjk3NzQ3OH0.JIYWuoFz5PWu8OlswBYWGi-uJ5u8u8v4UpxofhNNdVs"
    val invalidUserItsc: String = "invalid itsc"

    val testSociety: String = "test society"

}