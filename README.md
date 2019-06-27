# PowerBackupDroid

It is an Android application which takes the back up of your SMS and Call History and sends it to your own Server. It never stores the copy in Local DB or any other private servers.

### App Required Information

This app requires the following information for the initial configuration.

1. User Identifier -> It is any string which is used to identify the user or mobile Ex : Simon-Android-S8_plus:<mobile_number>
2. API -> Your API Ex: https://your-server.com/api/backup/sms
3. Security Key -> This security key is attached to your API request as a Authentication Header like Authorization : Bearer <Security key>
  
Sample Payload : 

```
{
    "sms": {
        "data": [
            {
              "id": "8381",
              "msg": "Play Rummy FREE! Rs.5,000 as Welcome Bonus http://1kx.in/KEbVbX Win Real Money!",
              "sourceNumber": "BT-WINWIN",
              "timestamp": 1561098395896,
              "userIdentifier" : "Simon-Android-S8_plus:987654526367"
            },
            {
            "id": "8380",
            "msg": "Dear Customer, This Number Available In Vodafone Postpaid For Instant Activation Dial",
            "sourceNumber": "+919876543210",
            "timestamp": 1561095428631,
             "userIdentifier" : "Simon-Android-S8_plus:987654526367"
            }
        ]
    }
}
```

Features : 

  1. Battery Consious
      Though PowerBackup reads your phone SMS once in 15 mins, It doesn't connect to your server until you have data to be pushed.
  2. Assured Delivery
      As it uses Job scheduler for the SMS backup, it will be backed up to your server without failure.
  3. Security
      It takes security code from the client app and adds it to the Authorization Header, So Unknown sourcs can't corrupt the data
  4. Better Resource Management
      As the backup requires the Internet Connection, This application won't run the service if you don't have internet connection. by         which Unnessary resource consumption is reduced


Note : As it is initial version it has only takes the backup of your SMS. In future version it also supports like Call History, Contacts ...

### Steps to use the app

1. Download the latest apk from releases -> [Click here](https://github.com/simonchius/PowerBackupDroid/releases)
2. Install the app in your device
3. Configure the backup server details in the app using the `Set Configuration` option

<img src="https://raw.githubusercontent.com/simonchius/PowerBackupDroid/readmechanges/screenshots/1.JPG" alt="" height="400" />

4. Provide the requested information for the server configuration (Providing User Id might help you identify the data in the server)

<img src="https://raw.githubusercontent.com/simonchius/PowerBackupDroid/readmechanges/screenshots/2.jpg" alt="" height="400" />

5. Once configured click on the `Backup Now` option to take a backup of all sms in your device to the server you configured.

                                                       #Thank You



  
    
