---
layout: page
title: User Guide
---

# Friends And Money (FAM) 
Keeping in touch with your friends and clients should be **easy and efficient**. 

FAM is a **desktop contact management app** built for student financial advisors. It helps you track your relationships, 
log interactions and schedule follow-ups in one place.

FAM is optimized for use via a **Command Line Interface (CLI)**. So if you type fast, you can manage your contacts **significantly faster** with FAM than with traditional apps. A **Graphical User Interface (GUI)** is provided too, so that you can have the best of both worlds. 

### Table Of Contents 
* [Quick start](#quick-start)
* [Features](#features)
* [FAQ](#faq)
* [Known issues](#known-issues)
* [Command summary](#command-summary)

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest `.jar` file from [here](https://https://github.com/AY2526S2-CS2103T-W12-4/tp/releases).

3. Copy the `.jar` file to a folder you would like to use as the _home folder_ to store your contact list.

4. Open a command terminal, `cd` into the folder you put the `.jar` file in, 
and run `java -jar fam.jar` to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

5. Type a command in the command box and press Enter to execute it. 
   Some example commands you can try:

   * `help` : Opens the help window. 
   
   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : 
   Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

6. Refer to [Features](#features) for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**Notes about the command format**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter, e.g. `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g. `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times, or zero times.<br>
  e.g. `[t/TAG]…​` can be omitted, or used as `t/friend`, `t/friend t/family`.

* Parameters can be in any order.<br>
  e.g. `n/NAME p/PHONE_NUMBER` is the same as `p/PHONE_NUMBER n/NAME`. 

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) 
will be ignored.<br>
  e.g. `help 123` will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands 
that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME p/PHONE_NUMBER [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags (including 0)
</div>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Editing a person : `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e. adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g. `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Viewing a person : `view`

Shows the specified person.

Format: `view INDEX`

* Shows the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `view 2` shows the 2nd person in the address book.
* `find Betsy` followed by `view 1` shows the 1st person in the results of the `find` command.

### Deleting a person : `delete`

Deletes the specified contact from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the number shown beside your contact's name. 
* The index **must be a positive integer** (e.g. 1, 2, 3, ...) and be within the valid range of contacts.
* **Confirmation message** will be shown before deletion. You will need to click `OK` to confirm the deletion.

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Adding a tag : `tagadd`

Adds a tag to an existing person in the address book.

Format: `tagadd INDEX [t/TAG]`

* Adds a tag to the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* Creates the tag if it does not already exist.
* If the tag exists, the addition of the tag will not be allowed.
* Only 1 tag can be added at a time.

Examples:
* `tagadd 1 t/friend` adds the tag `friend` to the 1st person in the address book.

### Removing a tag : `tagrm`

Removes a tag from an existing person in the address book.

Format: `tagrm INDEX [t/TAG]`

* Removes a tag from the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* Removes the tag if it exists for the person.
* If the tag does not exist, the deletion of the tag will not be allowed.
* Only 1 tag can be removed at a time.

Examples:
* `tagrm 1 t/friend` removes the tag `friend` from the 1st person in the address book.

### Clearing all entries : `clear`

Clears all entries from the contact list.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

FAM's data is saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

FAM's data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, FAM will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the FAM to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install FAM in the other computer. Overwrite the empty data file that it creates with your file that contains
the data of your previous FAM home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER [e/EMAIL] [a/ADDRESS] [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List** | `list`
**Help** | `help`
**View** | `view INDEX`<br> e.g., `view 3`
**Tag Add** | `tagadd INDEX [t/TAG]`<br> e.g., `tagadd 1 t/friend`
**Tag Remove** | `tagrm INDEX [t/TAG]`<br> e.g., `tagrm 1 t/friend`
