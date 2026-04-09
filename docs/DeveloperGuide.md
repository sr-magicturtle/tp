---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.)

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.
* manages **View Mode**, a UI state where full contact details are displayed. Refer to the
[View feature](#view-feature) section for implementation details.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagramv3.png" width="450" />

The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object). Each `Person` stores core contact details such as `Name`, `Phone`, `Address`, and `Email`, together with additional fields such as `Tag`, `Note`, `FollowUpDate`, and `Circle`.
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed'; for example, the UI can be bound to this list so that it automatically updates when the data in the list changes.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` object.
* does not depend on any of the other three components, as the `Model` represents data entities of the domain and should make sense on its own without depending on the other components.

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It keeps a `Tag` list in the `AddressBook`, which each `Person` references. This allows the `AddressBook` to maintain a single `Tag` object for each unique tag, instead of each `Person` storing separate `Tag` objects. In our current implementation, a `Person` may also store additional fields such as `Note`, `FollowUpDate`, and `Circle`.<br>

<img src="images/BetterModelClassDiagramV3.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Note Add feature
#### Implementation

The following sequence diagram shows how a `NoteAddCommand` is executed through the `Logic` component:

![NoteAddSequnceDiagram](images/NoteAddSequenceDiagram.png)


### Tag add feature

Users can add tags to contacts for categorisation, using the `tagadd` command. The implementation of this feature is shown in the sequence diagram below.

#### The sequence diagram
![Tag Add Sequence Diagram](images/TagAddSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `TagAddCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

### View feature

Users can view the full details of a contact using the `view` command. The implementation of this feature is shown
in the sequence diagram below.

#### The sequence diagram
![View Mode Sequence Diagram](images/ViewSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `ViewCommandParser`
should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

After `ViewCommand` executes, the app enters **View Mode**. `MainWindow` sets `isInViewMode = true` and
calls `handleViewPerson()` to display the contact's full details. While in View Mode, the contact is always shown at index 1. Running `add`, `list`, `delete`, `clear`, `find`, or `remind` exits View Mode and clears the detail panel. All other commands (e.g. `edit`, `note`, `tag`) keep the app in View Mode and refresh the detail panel with the latest contact details.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

**Primary target user**: _Student financial advisor_ who meets many people and needs to capture or retrieve contact info quickly.

* maintains a growing network
* often multitasking
* prefers fast keyboard commands
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* wants safe deletion to avoid accidental loss

**Value proposition**: AddressBook helps student financial advisors maintain and retrieve contacts quickly via keyboard-first commands, so they can follow up and manage relationships without relying on messy notes or slow UI workflows.

---

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                   | I want to …​                          | So that I can…​                                      |
| -------- |---------------------------| ------------------------------------- | ---------------------------------------------------- |
| `* * *`  | student financial advisor | add a contact                         | capture someone immediately after meeting them       |
| `* * *`  | student financial advisor | list all contacts                     | browse my network quickly                            |
| `* * *`  | student financial advisor | view a contact by index               | open full details quickly after listing/searching    |
| `* * *`  | student financial advisor | find contacts by name keyword         | retrieve people instantly                            |
| `* * *`  | student financial advisor | edit a contact’s details              | keep details accurate and fix typos quickly          |
| `* * *`  | student financial advisor | delete a contact with confirmation    | remove outdated entries safely                       |
| `* * *`  | student financial advisor | add a tag to a contact                | categorise contacts quickly                          |
| `* * *`  | student financial advisor | remove a tag from a contact           | keep tags clean and updated                          |
| `* *`    | student financial advisor | find contacts by notes keyword        | locate someone by remembered context                 |
| `* *`    | student financial advisor | list contacts filtered by tag         | target a group easily                                |
| `* *`    | student financial advisor | sort contacts by name                 | scan large lists more easily                         |
| `* *`    | user                      | see usage instructions/help           | refer to commands when I forget how to use the app   |
| `*`      | student financial advisor | log interactions with a contact       | track engagement history                             |
| `*`      | student financial advisor | set follow-up dates / reminders       | avoid forgetting to check in                         |

*{More to be added}*

---

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a contact with tags**

**MSS**

1.  User requests to add a contact with required details (e.g., name, phone).
2.  AddressBook requests any missing required fields (if any).
3.  User provides missing fields (if any).
4.  AddressBook creates the contact and shows a success message.
5.  User requests to add one or more tags to the newly added contact.
6.  AddressBook adds the tags and shows the updated contact.

    Use case ends.

**Extensions**

* 2a. User input is invalid (e.g., invalid phone/email format).

    * 2a1. AddressBook shows an error message.

      Use case resumes at step 1.

* 4a. A duplicate contact is detected (same name and phone number).

    * 4a1. AddressBook shows an error message.

      Use case ends.

---

**Use case: Find a contact by name keyword, then edit the contact**

**MSS**

1.  User requests to find contacts using a name keyword.
2.  AddressBook shows a list of matching contacts.
3.  User requests to view a specific contact from the list by index.
4.  AddressBook shows the full contact details.
5.  User requests to edit one or more fields of the contact.
6.  AddressBook updates the contact and shows the updated contact.

    Use case ends.

**Extensions**

* 2a. No contacts match the keyword.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 5a. The edited value is invalid (e.g., invalid email).

    * 5a1. AddressBook shows an error message.

      Use case resumes at step 4.

---

**Use case: Delete a contact**

**MSS**

1.  User requests to list contacts.
2.  AddressBook shows a list of contacts.
3.  User requests to delete a specific contact in the list by index.
4.  AddressBook asks for confirmation to delete the contact.
5.  User confirms the deletion.
6.  AddressBook deletes the contact and shows a success message.

    Use case ends.

**Extensions**
* 1a. User already knows the index of the contact to delete.

  Use case resumes at step 3.

* 2a. The contact list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 5a. User cancels the deletion.

  Use case ends.

---

**Use case: Remove a tag from a contact**

**MSS**

1.  User requests to find contacts using a name keyword.
2.  AddressBook shows a list of matching contacts.
3.  User requests to view a specific contact from the list by index.
4.  AddressBook shows the full contact details including tags.
5.  User requests to remove a tag from the contact.
6.  AddressBook removes the tag and shows the updated contact.

    Use case ends.

**Extensions**
* 1a. User already knows the index of the contact to untag.

  1a1. User requests to remove a tag from the contact by index.
  1a2. AddressBook removes the tag and shows the updated contact.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 5a. The tag does not exist on the contact.

    * 5a1. AddressBook shows an error message.

      Use case resumes at step 4.

*{More to be added}*

---

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` installed.
2.  Should work without requiring an installer (i.e., the product can be run directly from the delivered JAR/ZIP).
3.  Should be packaged as a single JAR file. If this is not possible, the deliverable should be a single ZIP file containing the JAR and any required files.
4.  Should store data locally in a human-editable text file, and the level of support for manual edits should be at least that of AB3.
5.  Should not use a DBMS for data storage.
6.  Should not depend on a remote server owned/maintained by the project team.
7.  Should be primarily object-oriented in design and implementation.
8.  Should target single-user usage (i.e., the data is intended for one user and is not designed for concurrent access by multiple users).
9.  Should be optimized for users who can type fast and prefer typing, such that most tasks can be completed faster via CLI commands than via mouse/GUI interactions.
10. Should be able to hold up to 1000 contacts without noticeable sluggishness in performance for typical usage.
11. The GUI should work well for screen resolutions `1920x1080` and higher at `100%` and `125%` scaling, and remain usable for `1280x720` and higher at `150%` scaling.
12. The deliverable file size should not exceed `100MB` for the product (JAR/ZIP).
13. The Developer Guide (DG) and User Guide (UG) should be PDF-friendly (e.g., no expandable panels, embedded videos, or animated GIFs).
14. The product should not require installation of any third-party software by the user. Any third-party libraries/services used should be free/open-source (or approved services), permissively licensed, and pre-approved by the teaching team.gh to allow new commands/features to be added without major rewrites.

*{More to be added}*

---

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, macOS
* **AddressBook**: The system that stores and manages contacts
* **Contact**: An entry representing a person, containing fields such as name, phone number, and email
* **Index**: The number shown in a listed result that identifies a specific contact in that displayed list
* **Keyword**: A search term used to find matching contacts (e.g., partial name match)
* **Tag**: A short label attached to a contact for categorisation
* **Confirmation**: A user action required to proceed with a destructive operation (e.g., delete)
* **Command**: A text instruction typed by the user to perform an operation in AddressBook
--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
