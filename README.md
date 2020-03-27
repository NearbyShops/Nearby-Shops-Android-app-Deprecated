Nearby Shops 
[![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=Nearby%20Shops%20Open%20Source%20Food%20Delivery%20and%20Hyperlocal%20app&url=https://github.com/NearbyShops/Nearby-Shops-End-User-Android-app&via=moraysumeet&hashtags=opensource,androiddev,fooddelivery,android,ecommerce)
=============

<img src="https://github.com/SumeetMoray/Nearby-Shops-End-User-Android-app/blob/master/media/nearby-shops-logo-small.png" width="80">    <a href="https://play.google.com/store/apps/details?id=org.nearbyshops.enduserappnew&hl=en"><img class="alignnone" src="https://goldtonemusicgroup.com/img/goldtone/main-page/news/playstore-badge.png" alt="Get it on Google Play" width="130" height="40" /></a> <a href="https://twitter.com/nearbyshopsapp?ref_src=twsrc%5Etfw" class="twitter-follow-button" data-show-count="false">
<img src="https://www.mathlearningcenter.org/sites/default/files/images/Follow%20on%20Twitter.png"
width="120">
</a>


How it works ? 
-----------------
Nearby Shops is a **free & Open Source**  Local Market Server. Anyone can self-host their local market Instance (server) & and create a free marketplace for food delivery and hyperlocal business in just 10 Minutes Installation. 

Website :  https://nearbyshops.org | Developers Guide: http://developer.nearbyshops.org


### Install with Docker 


    cd ~

    wget https://raw.githubusercontent.com/NearbyShops/Nearby-Shops-API/master/docker-compose.yml

    docker-compose up


Full installation guide available at https://developer.nearbyshops.org/installation/installation-guide-quick-docker.html



### Commercial Use Permitted under Single Market License

Good news for you ... !! Now you can run your own commercial business with our free open - source software. 
Please read more about about our Single Market License here https://blog.nearbyshops.org/single-market-license/



About Nearby Shops
--------------------

Nearby Shops is a Free Open-Source food delivery, grocery and hyperlocal app platform. You can install your self-hosted instance on Digital Ocean or AWS and get your grocery or food delivery market up and running in just 5 minutes.


Nearby Shops implements Alibaba's Grocery Online-to-Offline Concept. Customers can place and order from the app and pick it up in the store. Home Delivery is also available !

The installation guide and app customization guide is provided at http://developer.nearbyshops.org


Website :  https://nearbyshops.org | Developers Guide: http://developer.nearbyshops.org


<img src="https://nearbyshops.org/images/screenshots_usa/shops_list_san_francisco.png" width="208">   <img src="https://nearbyshops.org/images/screenshots_usa/order_detail_john_doe.png" width="208">   <img src="https://nearbyshops.org/images/items-in-shop-new.png" width="208"> 



## 🚩 Table of Contents
- [Concept](#concept)
- [Tech Stack and App Architecture](#tech-stack-and-app-architecture)
- [Features and highlights](#features-and-highlights)
- [Libraries Used](#libraries-used)
- [Third Party Integrations](#third-party-integrations)
- [Connect with Us](#community---need-help-)
- [Contributions Required](#contributions-welcome)
- [Development Instructions](#development-instructions)
- [License](#license)


Concept
----------
Nearby Shops is a Hyperlocal Shopping platform where a customer can buy directly from the shops available in his/her local area and get their orders delivered to their home or just pick the order up from the shop. 

In the new emerging world, customers are getting tired of going outside the home to shop every day. They would much prefer to simply place an order and have the products delivered to their home the same day. 

Conventional e-commerce has issues ... the delivery takes a long time and it's more difficult to trust unknown vendors whom you cannot see and meet. Hyperlocal e-commerce solves these issues because delivery is faster and customers can reach out to the vendors easily. 

Nearby Shops can also be used as a food Delivery platform where restaurants can use it to deliver food. 

Nearby Shops implements Alibaba's New Offline-to-Online Concept, where customers discover products online and then pick those products up from physical stores. 

![Online to Offline](https://www.innovationiseverywhere.com/wp-content/uploads/2015/05/what-is-o2o-online-to-offline-digital-ecommerce-retail-china-gmic-1-2.jpg)



Tech Stack and app Architecture
--------------------------------

Uses Android Jetpack and Google’s Recommended Architecture Patterns - Migration to MVVM is planned

Written in both Java and Kotlin : Migration to kotlin is planned and will be coming in near future

Built using Butterknife, Retrofit, Ok-HTTP, Picasso, Dagger, and the Mapbox Android SDK

User-Interface is made using the modular View-Holder Pattern which makes the UI blocks modular, easy to reuse, modify and understand.

The app uses Single Activity Architecture, where most of the screens are implemented as fragments and there are only 2-3 activities in the entire project.

The project has 3 Android apps. One app each for the end-user, shop-owner and adminstrator. There is also a JSON-based REST API on the backend. The source-code for the REST API is provided. 



Features and Highlights
----------------------------

Support for Multiple Currencies : You can set your locale and country and currency for your local market will be set accordingly.

Mobile First - Multi-Vendor Platform where customers can send orders to multiple Shops / Restaurants

Integrations for Sending SMS-OTP, E-mail and Push Notifications are available

Order Tracking, with live status updates for customers using E-mail, SMS and Push Notifications

Rating and Reviews for items and shops

Home delivery and pick-up from the shop (Online-to-Offline) Shopping Concept is Supported

Location based filtering is available, which means that customers will see only those shops which can deliver to their address -- other shops will be filtered out. 

Vendors / Shop-Owners can track orders through orders inventory and update the order progress !

By adding delivery staff, shop-owners can deliver orders by themselves. Shared delivery logistics will also be available in future releases of Nearby Shops. 

Billing and payments are supported. Shop-owners are billed according to the number of orders they deliver successfully. And Service Providers (Market Owners) can collect payments from the shop-owners. 

Nearby Shops Multi-Market Mode - you can add your local market to nearby shops market discovery service and your market becomes visible and accessible to Global audience of Nearby Shops app. Read more about Nearby Shops multi-market mode !



Libraries Used
---------------

Android-Jetpack, Retrofit, Ok-Http, Event-Bus, Picasso, butterknife, dagger2, U-Crop, Gesture Views, Smiley Rating, Mapbox Android SDK


Third Party Integrations
-------------------------
SMS-OTP using MSG91 (More Integrations coming soon ...)
E-mail using Simple-Java-Mail E-mail Library
Push Notifications using Firebase and One-Signal
Maps using Mapbox Android SDK and Google Maps
Payment Gateway (Coming Soon ...)

For more information please see our developer guide !


Connect With us - Need Help ? 
------------------------

If you want any help regarding anything. Feel free to contact us -- send a message through our forum or you can simply post an issue. Your issues will not be ignored and you will surely receive help. 

Discourse Forum - https://forum.nearbyshops.org

Follow us Twitter - https://twitter.com/nearbyshopsapp

Facebook - https://www.facebook.com/nearbyshops

Facebook Group - https://www.facebook.com/groups/1144257952430940/



Contributions Welcome
-----------------------------------------------

https://forum.nearbyshops.org/t/project-roadmap-and-contributions-required/34/4

Improved Search with Search Suggestions for android app

Develop a PWA and Web app for Nearby Shops

i18n Internationalization support

Payment Gateway Integrations

Performance Testing and Optimization

Suggestions and Improvements in App Architecture

Code Review and Architecture



Development Instructions
-------------------------

Use the latest stable version of android studio. 

If you are running your own server and do not want multiple markets. Please set multi-market mode to false and set your server URL in the PrefGeneral.java file. 




## Hiring Remote Workers - Be the part of Our International Community !

Benefit from the flexible working hours and work from anywhere in the world. We are looking for Volunteers who can help us in the following areas

1. Raise funds for the Project - Example : Create Kickstarter campaign and raise donations
2. Content Creators 
   - Create Images and Video for publicity purposes
   - Write articles to create publicity
3. Growth Hackers - help us grow the platform 
4. Distribution Experts / Digital Marketing Experts / Social Media Experts
5. Developers - Android, Web, PostgreSQL, Java
6. Any other way you want to help – Suggestions and Ideas are Welcome !

If you're interested please get in touch with us on https://forum.nearbyshops.org




License
=======

Nearby Shops Single Market License

Good news for you ... !! Now you can run your own commercial business with our free open - source software. 
Please read more about about our Single Market License here https://blog.nearbyshops.org/single-market-license/

Copyright (c) 2020 Nearby Shops

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

