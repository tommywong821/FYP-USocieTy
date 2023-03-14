from customtkinter import *
from tkinter import *
from tkinter import ttk
from datetime import datetime
import requests
from PIL import Image
from smartcard.System import readers
import base64
from Crypto.Cipher import PKCS1_v1_5 as PKCS1_cipher
from Crypto.PublicKey import RSA
import json




### Global variable ###
url = "https://ngok3fyp-backend.herokuapp.com/attendance"
dummyEventID1 = "b33f74e2-e08d-4c28-b023-466f11e0d875"
dummyEventID2 = "75abe7ef-c9cf-45bb-a7e2-94252c604e21"
dummyStudentID = "984e6b48-fdc1-4d55-88b0-8fd3fee27983"
dummyItsc = "asdfg"




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
            eventsArray.append(event['eventName'])

        #combo box
        self.comboBox = CTkOptionMenu(master=self, values=eventsArray, command=self.comboBox_event, width=200, height=30)
        self.comboBox.pack(pady=(40,30))
        self.comboBox.set(eventsArray[0])
        
        #button
        self.insertBtn = CTkButton(master=self, text="Insert Record", command=self.insertBtn_event, height=30)
        self.insertBtn.pack(pady=(0,30))
        self.deleteBtn = CTkButton(master=self, text="Delete Record", command=self.deleteBtn_event, state="disabled", height=30)
        self.deleteBtn.pack(pady=(0,40))
    #TODO @!!!@@
    def comboBox_event(self, choice):
        app.listFrame.refreshRecord()
    
    def deleteBtn_event(self):
        try:
            app.listFrame.deleteRecord()
        except:
            pass

    def insertBtn_event(self):
        inputDialog = CTkInputDialog(text="Please type ITSC ID or UID:", title="Insert Record")
        centerWindow(inputDialog)
        inputStr = inputDialog.get_input()
        #insert uid
        if(checkNumeric(inputStr)):
            app.listFrame.insertRecord(inputStr, getITSC(inputStr))
            postEvent(inputStr)
        #insert student name
        elif (checkNumeric(inputStr) == False):
            app.listFrame.insertRecord(getUID(inputStr), inputStr)
            postEvent(getUID(inputStr))
        else:
            pass



class ListFrame(CTkFrame):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        label = CTkLabel(self, text="Attendance Record")
        label.pack(pady=(0,0))
        self.listBox = ttk.Treeview(self, columns=["1","2","3"], show="headings", selectmode="browse", height=20)
        self.listBox.pack(side='left',padx=(10,0), pady=(0,10))
        self.listBox.column("1",anchor=CENTER, stretch=NO)
        self.listBox.heading("1", text="UID")
        self.listBox.column("2", anchor=CENTER, stretch=NO)
        self.listBox.heading("2", text="Name")
        self.listBox.column("3", anchor=CENTER, stretch=NO)
        self.listBox.heading("3", text="Time")
        self.listBox.bind("<<TreeviewSelect>>", self.treeview_callback)
        listBoxScrollBar = CTkScrollbar(self, command=self.listBox.yview)
        listBoxScrollBar.pack(fill='y',side='right', padx=(0,5), pady=5)
        self.listBox.config(yscrollcommand=listBoxScrollBar.set)

    def insertRecord(self, uid, name):
        self.listBox.insert("", "end",values=(uid, name, datetime.now().strftime("%Y-%m-%d %H:%M")))
        # scroll to bottom = 1, scroll to top = 0
        self.listBox.yview_moveto(1) 

    def deleteRecord(self):
        self.listBox.delete(self.listBox.selection())
        #TODO delete request

    #TODO to be fixed
    def refreshRecord(self):
        #delete all records
        self.listBox.delete(*self.listBox.get_children())
        #re-render records
        


    def treeview_callback(event):
        isSelectedItem = str(app.listFrame.listBox.selection()).startswith("('")
        if(isSelectedItem):
            app.controlFrame.deleteBtn.configure(state="normal")
        else:
            app.controlFrame.deleteBtn.configure(state="disabled")



class App(CTk):
    def __init__(self):
        super().__init__()
        self.geometry("1000x500")
        self.title("Attendance-chekcing System")
        self.resizable(False, False)     
        left_pane = PanedWindow(self, orient="vertical",sashpad=5)
        left_pane.pack(side="left", anchor="n",padx=(30,0), pady=18, ipadx=50)
        self.rfidFrame = RFIDFrame(self)
        self.controlFrame = ControlFrame(self)
        left_pane.add(self.rfidFrame)
        left_pane.add(self.controlFrame)
        self.listFrame = ListFrame(self)
        self.listFrame.pack(pady=20)

        self.listFrame.insertRecord("1234","test1")
        self.listFrame.insertRecord("5678","test2")
        self.listFrame.insertRecord("9876","test3")



### utilities ###

def checkRFIDReader():
    try:
        reader = readers()[0]
        SELECT = [0xFF, 0xCA, 0x00, 0x00, 0x00]
        connection = reader.createConnection()
        connection.connect()
        data, sw1, sw2 = connection.transmit( SELECT)
        if (data):
            #4byte UID
            uid = str(data[0])+str(data[1])+str(data[2])+str(data[3])
            receivedRFIDSignal(uid)
    except:
        pass
    app.after(1000, checkRFIDReader)

def receivedRFIDSignal(uid):
    UIDfromReader = uid
    app.listFrame.insertRecord(UIDfromReader, getITSC(UIDfromReader))
    app.rfidFrame.changeImage("rfid2.png")
    postEvent(UIDfromReader)
    app.rfidFrame.changeImage("rfid1.png")

def centerWindow(window):
    ws = app.winfo_screenwidth()
    hs = app.winfo_screenheight()
    x = (ws/2) - (1000/2)
    y = (hs/2) - (500/2)  
    window.geometry("%d+%d" % (x, y))

def checkNumeric(input):
    if input.isnumeric():
        return True
    if input.isalpha():
        return False

def getKey(key_file):
    with open(key_file) as f:
        data = f.read()
        key = RSA.importKey(data)
    return key

def encryptData(msg):
    public_key = getKey('rsa_public_key.pem')
    cipher = PKCS1_cipher.new(public_key)
    encrypt_text = base64.b64encode(cipher.encrypt(bytes(msg.encode("utf8"))))
    return encrypt_text.decode('utf-8')

def postEvent(uid):
    #get selected event ID to post
    #eventID = app.controlFrame.comboBox.get()

    header = {'Content-Type': 'application/json'}
    innerJson = '{"eventId": "' +str(dummyEventID2)+ '", "studentId": "' +str(dummyStudentID)+ '", "userItsc": "' +str(dummyItsc)+ '"}'
    jsonStr = innerJson

    #encrypt part
    #jsonStr = encryptData(json.dumps(innerJson))

    jsonData = {'data': jsonStr}
    response = requests.post(url, data=jsonStr, headers=header)
    print("JSON data: ",innerJson)
    #print("EncryptedJSON: ",jsonStr)
    #print("Post data: ",json.dumps(jsonData))
    print("Status Code: ", response.status_code)
    print("JSON Response: ", response)

def getEvent():
    global events
    events = requests.get(url).json()
    return events

def getEventItems(eventName, item):
    eventsArray = []
    for event in events:
        if(event['eventName'] == eventName):
            eventsArray.append(event[item])
    return eventsArray

def deleteEvent(studentID, eventID):
    print(requests.delete(url, params= {"studentUuid":dummyStudentID , "eventUuid": dummyEventID1}))

#TODO to be fixed
def getUID(itscID):
    url = "https://ngok3fyp-backend.herokuapp.com/student"
    uid = requests.get(url, params={"itsc": itscID}).json()
    return uid['uuid']

#TODO to be fixed
def getITSC(uid):
    url = "https://ngok3fyp-backend.herokuapp.com/student"
    itsc = requests.get(url, params={"uuid": uid}).json()
    return itsc['itsc']





### main ###

if __name__ == "__main__":
    set_appearance_mode("light")
    getEvent()
    print(getITSC(dummyStudentID))
    print(getUID(dummyItsc))
    app = App()
    app.after(1000, checkRFIDReader())
    app.mainloop()