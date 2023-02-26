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

        #combo box
        #TODO get user's holded event
        self.comboBox = CTkOptionMenu(master=self, values=["test event 1", "test event 2", "test event 3"], command=comboBox_event, width=200, height=30)
        self.comboBox.pack(pady=(40,30))
        self.comboBox.set("test event 1")
        
        #button
        self.insertBtn = CTkButton(master=self, text="Insert Record", command=insertBtn_event, height=30)
        self.insertBtn.pack(pady=(0,30))
        self.deleteBtn = CTkButton(master=self, text="Delete Record", command=deleteBtn_event, state="disabled", height=30)
        self.deleteBtn.pack(pady=(0,40))

def comboBox_event(choice):
    print(app.controlFrame.comboBox.get())

def deleteBtn_event():
    try:
        app.listFrame.deleteRecord()
    except:
        pass

def insertBtn_event():
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
        self.listBox.bind("<<TreeviewSelect>>", treeview_callback)
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



### function ###

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
    eventID = app.controlFrame.comboBox.get()
    url = "https://ngok3fyp-backend.herokuapp.com/attendance"
    innerJson = {"eventId": eventID,"studentId": uid}
    jsonData = {"data" : innerJson}
    jsonStr = encryptData(json.dumps(jsonData))
    response = requests.post(url, data=jsonStr)
    print("JSON data: ",jsonData)
    print("EncryptedJSON: ",jsonStr)
    print("Status Code: ", response.status_code)
    print("JSON Response: ", response.json())

#TODO with APIs
def getEvent():
    pass

#TODO with APIs
def getUID(itscID):
    return "8888"

#TODO with APIs
def getITSC(uid):
    return "test_ITSC"



### main ###

if __name__ == "__main__":
    set_appearance_mode("light")
    app = App()
    app.after(1000, checkRFIDReader())
    app.mainloop()