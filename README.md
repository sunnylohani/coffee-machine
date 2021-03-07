# Coffee Machine

This maven project simulates a working coffee machine. It takes input from a JSON file and provides output to console. The JSON file contains the initial inventory as well as items in the order.

## Implementation

The number of machine outlets in the input translates to concurrent worker threads. These threads listen to a blocking queue for orders and subsequently process them. The inventory has lock protected methods for updation and refill. There is a scheduled executor thread for inventory indicator which checks for ingredients running low in the inventory. Besides, it also provides option to refill ingredients.

## Running the project

The project has `CoffeeMachine.java` which has the main method. Just by running this class, the provided JSON input file can be read and processed.

## Unit test
The project can also be run and tested by running the JUnit provided in the tests folder.