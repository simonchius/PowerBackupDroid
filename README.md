# PowerBackupDroid

It is an Android application which takes the back up of your SMS and Call History and sends it to your own Server. It never stores the copy in Local DB or any other private servers.

#App Required Information

This app requires the following information for the initial configuration.

1. User Identifier -> It is any string which is used to identify the user or mobile Ex : Simon-Android-S8_plus:<mobile_number>
2. API -> Your API Ex: https://your-server.com/api/backup/sms
3. Security Key -> This security key is attached to your API request as a Authendication Header like Authorization : Bearer <Security key>
  
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

Note : As it is initial version it has only takes the backup of your SMS. In future version it also supports like Call History, Contacts ...


#Thank You



  
    
