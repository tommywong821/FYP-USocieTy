package ngok3.fyp.backend.event

import org.springframework.stereotype.Service

@Service
class EventService {
    fun getAllSocietyEvent(sid: String, pageNum: Int, pageSize: Int) {
        if (sid.isBlank()) {
            //get all event

        }
        //get all event from sid
    }
}