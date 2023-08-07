# UberIF

A Java app with GUI where you can import a map from an xml file and a tour from an xml file and the shortest/fastest path will be calculated.

## How to launch the application:

JDK 1.8 is required.

Simply open the project in IntelliJ and use the prepared run configuration.

- Go on Map --> Import Map --> Choose `largeMap`
- Go on Delivery --> Import Tour --> Choose `requestsMedium5`

## Error handling cases when importing a file:

Quoted message is the message shown to the user.

### When importing a map:

- When we select a file that is not xml: "Please only select .xml files".
- When there are no <intersection\> in the file: "No intersections found in file".
- If an <intersection\> doesn't have id or latitude or longitude attribute we consider that it is missing important data, and we ignore that intersection.
- If all <intersection\> in the file lack important data: "All intersections in the file lack important data".
- When there are no <segment\> in the file: "No segments found in file".
- If a <segment\> doesn't have origin or destination or length attribute we consider that it is missing important data, and we ignore that segment.
- If all <segment\> in the file lack important data: "All segments in the file lack important data".

### When importing a planning request (tour):

- When we select a file that is not xml: "Please only select .xml files".
- If the file doesn't have <depot\>: "Depot not found".
- If <depot\> doesn't have address attribute: "Depot address not specified".
- If <depot\> doesn't have departureTime attribute: "Departure time not specified".
- When there are no <request\> in the file: "No requests found in file".
- If a <request\> doesn't have pickupId or deliveryId attribute we consider that it is missing important data, and we ignore that request.
- If all <request\> in the file lack important data: "All requests in file lack important data".
- If the user imports a small map like smallMap.xml then imports a tour that has requests with intersections not figuring on the map like requestsLarge7.xml :"The map is too small for the requests imported, please use a bigger map."
