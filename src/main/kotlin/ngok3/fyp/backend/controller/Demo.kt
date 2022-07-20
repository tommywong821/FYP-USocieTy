package ngok3.fyp.backend.controller

class Demo(
    var message: String? = null,
    var lastEditUser: String? = null,
) {
    constructor(demo: String) : this() {
        this.message = demo
    }
}
