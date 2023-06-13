from customtkinter import *
from tkinter import *
from tkinter import ttk
import requests
from PIL import Image
from smartcard.System import readers
import base64
from Crypto.Cipher import PKCS1_v1_5 as PKCS1_cipher
from Crypto.Cipher import PKCS1_OAEP
from Crypto.PublicKey import RSA
from datetime import datetime
import json




### Global variable ###
dummyEventID1 = "b33f74e2-e08d-4c28-b023-466f11e0d875"
dummyEventID2 = "75abe7ef-c9cf-45bb-a7e2-94252c604e21"
dummyStudentID = "984e6b48-fdc1-4d55-88b0-8fd3fee27983"
dummyItsc = "asdfg"
# this pair not working for now
dummyStudentID2 = "a6d4e35a-3765-481c-80af-b7a8fc05ec0e"
dummyItsc2 = "qwert"
#global events
#global attendance




### layout ###
class RFIDFrame(CTkFrame):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        
        label = CTkLabel(self, text="RFID Reader")
        label.pack(padx=50, pady=10)

        #image
        rfid_image = CTkImage(light_image=Image.open("rfid1.png"),
                                  dark_image=Image.open("rfid1.png"),
                                  size=(150, 150))
        self.label = CTkLabel(self, image=rfid_image, text="")
        self.label.pack(pady=(0,20))

    def changeImage(self, image: str):
        rfid_image = CTkImage(light_image=Image.open(image),
                                  dark_image=Image.open(image),
                                  size=(150, 150))
        self.label.configure(image = rfid_image)
        app.update()



class ControlFrame(CTkFrame):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        
        eventsArray = []
        for event in events:
            eventsArray.append(event['name'])

        #combo box
        self.comboBox = CTkOptionMenu(master=self, values=eventsArray, command=self.comboBox_event, width=200, height=30)
        self.comboBox.pack(pady=(40,30))
        self.comboBox.set(eventsArray[0])
        
        #button
        self.insertBtn = CTkButton(master=self, text="Insert Record", command=self.insertBtn_event, height=30)
        self.insertBtn.pack(pady=(0,30))
        self.deleteBtn = CTkButton(master=self, text="Delete Record", command=self.deleteBtn_event, state="disabled", height=30)
        self.deleteBtn.pack(pady=(0,40))
        #for mac user, please change above line to 
        #self.deleteBtn.pack(pady=(0,0))

    def comboBox_event(self, choice):
        global comboBoxChoice
        comboBoxChoice = choice
        getAllAttendance()
        app.listFrame.refreshRecord(choice)
    
    
    def deleteBtn_event(self):
        eventID = getEventID(str.strip(comboBoxChoice))
        studentID = app.listFrame.listBox.item(app.listFrame.listBox.focus())['values'][0]
        if(deleteAttendance(studentID, eventID) == 202): #HTTP 202 == delete
            app.listFrame.deleteRecord()



    def insertBtn_event(self):
        inputDialog = CTkInputDialog(text="Please type Student UID:", title="Insert Record")
        centerWindow(inputDialog)
        inputStr = inputDialog.get_input()
        if(inputStr == "" or inputStr == None):
            return
        if(postAttendance(inputStr, getITSC(inputStr)) == 201): #HTTP 201 == create
            currentDate = datetime.now().strftime("%Y-%m-%dT%H:%M:%S.%f")[:-3]
            app.listFrame.insertRecord(inputStr, getITSC(inputStr) , currentDate)

    #Since the CTkInputDialog library is bugged
    #Please directly modify the library
    # self._cancel_button = CTkButton(master=self,
    #                                     width=100,
    #                                     border_width=0,
    #                                     fg_color=self._button_fg_color,
    #                                     hover_color=self._button_hover_color,
    #                                     text_color=self._button_text_color,
    #                                     text='Cancel',
    #                                     command=self._cancel_event) <------
    

    def initListFrame(self):
        global comboBoxChoice
        comboBoxChoice = app.controlFrame.comboBox.cget("values")[0]
        app.listFrame.refreshRecord(comboBoxChoice)



        
class ListFrame(CTkFrame):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        label = CTkLabel(self, text="Attendance Record")
        label.pack(pady=(0,0))
        self.listBox = ttk.Treeview(self, columns=["1","2","3"], show="headings", selectmode="browse", height=20)
        self.listBox.pack(side='left',padx=(10,0), pady=(0,10))
        self.listBox.column("1",anchor=CENTER, stretch=NO, width=300)
        self.listBox.heading("1", text="UID")
        self.listBox.column("2", anchor=CENTER, stretch=NO, width=100)
        self.listBox.heading("2", text="Name")
        self.listBox.column("3", anchor=CENTER, stretch=NO, width=200)
        self.listBox.heading("3", text="Time")
        self.listBox.bind("<<TreeviewSelect>>", self.treeview_callback)
        listBoxScrollBar = CTkScrollbar(self, command=self.listBox.yview)
        listBoxScrollBar.pack(fill='y',side='right', padx=(0,5), pady=5)
        self.listBox.config(yscrollcommand=listBoxScrollBar.set)

    def insertRecord(self, uid, name, date):
        self.listBox.insert("", "end",values=(uid, name, date))
        # scroll to bottom = 1, scroll to top = 0
        self.listBox.yview_moveto(1) 

    def deleteRecord(self):
        self.listBox.delete(self.listBox.selection())

    def refreshRecord(self, eventName):
        #delete all records
        self.listBox.delete(*self.listBox.get_children())
        #re-render records
        tuples = getAttendance(eventName)
        for attendance in tuples:
            (uid, name, date) = attendance
            app.listFrame.insertRecord(uid, name, date)
        
    def treeview_callback(self, event):
        isSelectedItem = str(app.listFrame.listBox.selection()).startswith("('")
        if(isSelectedItem):
            app.controlFrame.deleteBtn.configure(state="normal")
        else:
            app.controlFrame.deleteBtn.configure(state="disabled")




class App(CTk):
    def __init__(self):
        super().__init__()
        self.geometry("1000x500")
        self.title("USocieTy Attendance-checking System")
        self.resizable(False, False)     
        self.iconbitmap("app_icon.ico")
        left_pane = PanedWindow(self, orient="vertical",sashpad=5)
        left_pane.pack(side="left", anchor="n",padx=(30,0), pady=18, ipadx=50)
        self.rfidFrame = RFIDFrame(self)
        self.controlFrame = ControlFrame(self)
        left_pane.add(self.rfidFrame)
        left_pane.add(self.controlFrame)
        self.listFrame = ListFrame(self)
        self.listFrame.pack(pady=20)




### utilities ###

def checkRFIDReader():
    try:
        reader = readers()[0]
        SELECT = [0xFF, 0xCA, 0x00, 0x00, 0x00]
        connection = reader.createConnection()
        connection.connect()
        data, sw1, sw2 = connection.transmit(SELECT)
        if (data):
            #4byte UID
            uid = str(data[0])+str(data[1])+str(data[2])+str(data[3])
            receivedRFIDSignal(uid)
    except:
        pass
    app.after(1000, checkRFIDReader)

def receivedRFIDSignal(uid):
    try:
        pair = getUidByCard(uid)
        app.rfidFrame.changeImage("rfid2.png")
        studentID = pair[0]
        itsc = pair[1]
        response = postAttendance(studentID, itsc)
        app.rfidFrame.changeImage("rfid1.png")
        if(response == 201):
            currentDate = datetime.now().strftime("%Y-%m-%dT%H:%M:%S.%f")[:-3]
            app.listFrame.insertRecord(studentID, itsc, currentDate)
    except Exception as e: print(e)

def centerWindow(window):
    ws = app.winfo_screenwidth()
    hs = app.winfo_screenheight()
    x = (ws/2) - (1000/2)
    y = (hs/2) - (500/2)  
    window.geometry("%d+%d" % (x, y))

def getKey(key_file):
    with open(key_file) as f:
        data = f.read()
        key = RSA.importKey(data)
    return key


def encryptData(msg):
    private_key = getKey('rsa_private_key.pem')
    cipher = PKCS1_cipher.new(private_key)
    encrypt_text = base64.b64encode(cipher.encrypt(bytes(msg.encode("utf8"))))
    return encrypt_text.decode('utf-8')


def postAttendance(studentID, itsc):
    url = "https://ngok3fyp-backend.herokuapp.com/attendance"
    header = {'Content-Type': 'application/json'}
    currentDate = datetime.now().strftime("%Y-%m-%dT%H:%M:%S.%f")[:-3] + "Z"
    innerJson = '{"eventId": "' +str(getEventID(str.strip(comboBoxChoice)))+ '", "studentId": "' +str(studentID)+ '", "userItsc": "' +str(itsc)+ '", "currentTime": "'+currentDate+'"}'
    jsonStr = encryptData(innerJson)
    response = requests.post(url, json=json.dumps(jsonStr), headers=header)
    print("Status Code: ", response.status_code)
    getAllAttendance()
    return response.status_code

def getAllEvent():
    url = "https://ngok3fyp-backend.herokuapp.com/event"
    global events
    events = requests.get(url, params={"pageNum": 0, "pageSize": 10}).json()
    return events

def getEventID(eventName):
    for event in events:
        if(str.strip(event['name']) == eventName):
            return event['id']

def deleteAttendance(studentID, eventID):
    url = "https://ngok3fyp-backend.herokuapp.com/attendance"
    getAllAttendance()
    encryptStudentID = encryptData(studentID)
    encryptEventID = encryptData(eventID)
    response = requests.delete(url, params= {"studentUuid":encryptStudentID , "eventUuid": encryptEventID})
    print("Status Code: ", response.status_code)
    return response.status_code

def getAllAttendance():
    url = "https://ngok3fyp-backend.herokuapp.com/attendance"
    global attendances
    attendances = requests.get(url).json()
    return attendances

def getAttendance(eventName):
    attendanceArray = []
    for attendance in attendances:
        if(attendance['eventName'] == eventName):
            tuples = (attendance['studentUuid'], attendance['studentItsc'], attendance['attendanceCreatedAt'])
            attendanceArray.append(tuples)
    return attendanceArray

def getUID(itscID):
    url = "https://ngok3fyp-backend.herokuapp.com/student"
    uid = requests.get(url, params={"itsc": itscID}).json()
    return uid['uuid']

def getITSC(uid):
    url = "https://ngok3fyp-backend.herokuapp.com/student"
    itsc = requests.get(url, params={"uuid": uid}).json()
    return itsc['itsc']

#return both uid and itsc
def getUidByCard(cardID):
    url = "https://ngok3fyp-backend.herokuapp.com/student"
    uid = requests.get(url, params={"cardId": cardID}).json()
    pair = [uid['uuid'], uid['itsc']]
    return pair

def initAPIs():
    getAllEvent()
    getAllAttendance()

def initUI():
    tuplesArray = getAttendance(events[0])
    for attendance in tuplesArray:
        (uid, name, date) = attendance
        app.listFrame.insertRecord(uid, name, date)
    app.controlFrame.initListFrame()



### main ###

if __name__ == "__main__":
    set_appearance_mode("light")
    initAPIs()
    app = App()
    initUI()
    app.after(1000, checkRFIDReader())
    app.mainloop()