# Design
## Design Layout
This app primarily uses fragments with very little activty changing, most of the content can be found on the Home Activity with the main fragments being placed into a tab layout that can be swiped through with ease. The workout assistance are in their own activity to seperate them from the social media aspect of the app.
## Technologies
This app uses a couple of different technologies such as;
* Firebase
* Google Maps
* Mobile Permissions

### Firebase (https://console.firebase.google.com/u/0/project/cs4084-project-fae83/overview)
This app uses firebase for a lot of different functions
#### Authentication
The Email login is handled by the autentication functions in Firebase
#### Storage
The storage aspect of Firebase is used to store the pictures uploaded from the app. It is also used to hold a textfile with all of a profiles relevant informattion like their bio, goal or link to their profile picture.
#### Firestore
Firestore is used to keep track of a lot of different things.  
* It is used to save posts and links to pictures for the post if an image is uploaded with it

* It is used to save Things that need quick access such as display names and notifications

* It is used to save peoples friends lists

### Google Maps
Google Maps is used to track a persons run it also traces the rout online
### Mobile Permissions
This app uses a few permissions such as accessing location for the run or phones internal memory to access its pictures.(It uses a cropper to help crop these images)  
It also uses the phones vibrater when doing interval training

## Lessons Learned
* There are a lot of things that we tried to over complicate. A big take away from this is that if something seems too hard there is probably an easier way to do it.
* Another lesson that was learned was to better abstract what the task we want to complete it. For example, to get a full view of the run you do not need to know every point just the furthest north, sout, east and west
