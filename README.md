This is a SpaceX Dragon Rockets Repository - it can be used to execute different operations on rockets and missions. The available operations are:
- adding a new rocket - it requires to specify the name of a rocket
- adding a new mission - it requires to specify the name of the mission
- changing rocket status - available statuses are:
  - "On ground" - it is possible to change to this status if a rocket has no mission assigned
  - "In space" - it is possible to change to this status if a rocket has a mission assigned
  - "In repair" - if the rocket has a mission assigned the status of the mission will change to "Pending"
- assigning mission to rocket - rocket can have only one mission
- assigning rockets to mission - a mission can have more than one rocket assigned
- ending the mission - this way we change mission's status to "Ended" and unassign all the rockets that it previously had
- getting summary of missions - the missions are displayed in the console ordered by number of rockets assigned, if the number of rockets is the same they are ordered alphabetically in descending order
- getting all the rockets in the form of map - the name of the rocket is the key and the rocket itself is the value
- getting all the missions in the form of map - the name of the mission is the key and the mission itself is the value

Rockets and missions are stored in maps - with name as the key and object as value.
For missions available statuses are:
- "Scheduled" - when it was just created and has no rockets assigned
- "Pending" - if one of assigned rockets is "In repair"
- "In progress" - if none of the assigned rockets is "In repair"
- "Ended" - the final stage, no rockets are assigned any more

This is a simple solution - it doesn't use any frameworks or databases. Implementations of the methods are stored in DragonRocketRepository class, which implements RocketRepository interface. At the moment it is the only implementation of this interface, but it is possible to add more implementations for example for different type of rockets. The tests were also added to check all the edge cases for the repository methods.
