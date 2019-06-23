Nearby Shops End-User android app
====================================


<img src="https://i1.wp.com/nearbyshops.org/wp-content/uploads/2018/12/Untitled-design-e1545575054544.png" width="140"> 

Nearby Shops is an Open-Source food delivery, grocery and Hyperlocal platform. You can install it on Digital Ocean 
and get your grocery or food delivery market up and running in just 5 $ per month hosting fee. 

Nearby Shops implements Alibaba's Grocery online-and-offline Concept. Where customers can place order from the app and pick their orders from the shop. 

The installation guide and app customization guide is provided at http://developer.nearbyshops.org

Website :  https://nearbyshops.org | Developers Guide: http://developer.nearbyshops.org


<a href="https://play.google.com/store/apps/details?id=org.nearbyshops.enduserappnew"><img class="alignnone" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" alt="Get it on Google Play" width="219" height="90" /></a>


<img src="https://github.com/SumeetMoray/Nearby-Shops-End-User-Android-app/blob/master/media/items_new_compressed.gif" width="208">   <img src="https://github.com/SumeetMoray/Nearby-Shops-End-User-Android-app/blob/master/media/orders_compressed.gif" width="208">   <img src="https://github.com/SumeetMoray/Nearby-Shops-End-User-Android-app/blob/master/media/shops_compressed.gif" width="208"> 

Tech Stack and app Architecture
--------------------------------

Uses Android Jetpack and Google’s Recommended Architecture Patterns - Migeration to MVVM is planned

Written in both Java and Kotlin : Migeration to kotlin is planned and will be coming in future

Built using Butterknife, Retrofit, Ok-HTTP, Picasso, Dagger, Mapbox android SDK

The project has 3 android apps each for End-User, Shop-Owner and adminstrator and a JSON based REST API at the backend. The Source for REST API is provided. 


Features and Highlights
----------------------------

Mobile Based - Multi-Vendor Platform where customers can send orders to multiple shops / Restaurants

Integrations for Sending SMS-OTP, E-mail and Push Notifications are available

Order Tracking with Live status updates for Customer using e-mail, SMS and Push Notifications

Rating and Reviews for Items and Shops

Home Delivery and Pick From Shop (Online-to-Offline) Shopping Concept is Supported

Location Based Filtering is available which means that customers will see only those shops which can deliver to their address and other shops will get filtered out. 

Vendors / Shop-Owners can track order's through orders inventory and update the order progress !


Screenshots
-----------------

<img src="https://nearbyshops.org/images/3.png" width="208">   <img src="https://nearbyshops.org/images/5.png" width="208">   <img src="https://nearbyshops.org/images/6.png" width="208">




Libraries Used
---------------

Android-Jetpack, Retrofit, Ok-Http, Event-Bus, Picasso, butterknife, dagger2, U-Crop, Gesture Views, Smiley Rating, Mapbox Android SDK


Third Party Integrations
-------------------------

Nearby Shops has latest integrations for SMS-OTP verification(MSG91), E-mail (Simple-Java-Mail), One-Signal and Firebase (for push notifications), Mapbox Android SDK for Maps


Community - Need Help ? 
------------------------

If you want any help regarding anything. Feel free to contact us. Send a message through our forum or you can simply post an issue. Your issues will not be ignored and you will surely receive help. 

Reach out to us - Please Visit - https://forum.nearbyshops.org

Get paid to Contribute
--------------------------

Developer time is valuable and its not fair to expect developers to contribute to the project without getting reasonably compensated. 

Therefore we are planning to arrange funds which will help us compensate those who contribute to the project. For more information please visit https://forum.nearbyshops.org and get in touch with us !


Source code for Shop-Owner, Admin app and Server Side
-------------------------------------------------------
If you want to see source code for Shop-Owner app, Admin-app and server side the links are provided below

Shop-Owner app : https://github.com/SumeetMoray/Nearby-Shops-Shop-Owner-Android-app

Admin app : https://github.com/SumeetMoray/Nearby-Shops-Admin-App

Source code for Server Side JSON Based REST API is available in this repository https://github.com/SumeetMoray/Nearby-Shops-API



Contributions Requied in following Areas 
-------------------------------------------

https://forum.nearbyshops.org/t/project-roadmap-and-contributions-required/34/4


Development Instructions
-------------------------

If you're a developer looking to work on the source code follow these instructions to start developing !

git clone https://github.com/SumeetMoray/Nearby-Shops-End-User-Android-app.git

This project uses mapbox android sdk which requires mapbox style url. To add the style url in the project open this project in the android studio and add the Mapbox Style URL in the local.properties file. 

mapbox_style_url="your-mapbox-style-url"




License
=======

    Copyright 2017-19 Bluetree Software LLP, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

