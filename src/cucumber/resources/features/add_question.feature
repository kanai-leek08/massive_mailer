Feature: Add Question
  As a trainer, I want to add new questions,
  so that students can take test.

  @developing
  Scenario: trainer add a question with 2 options
    Given a trainer enters the question edit page
    When he add a question description "what is 1+1?"
    And add question option1 "must be 3!"
    And add question option2 "of course 2."
    And he set the option 2 as the correct answer
    And he set the advise "you should read a math book"
    And he press the "<add_button>"
    And he enters the question list page
    Then he should see the question "what is 1+1?" in the question list
    And the option 1 should be "must be 3!"
    And the option 2 should be "of course 2."
    And option 2 (the correct answer) is highlighted
    And the advise should be "you should read a math book"

  @developing
  Scenario: Adding a new question with over length description
    When a trainer enters the question edit page
    And Trainer add a new question that the "<description>" is over length
    Then Error message "The description is over length" appears and stay at the same page

  @developing
  Scenario: Adding a new question with over length option
    When a trainer enters the question edit page
    And Trainer add a new question that the "<option>" is over length
    Then Error message "The option is over length" appears and stay at the same page

  @developing
  Scenario: Adding a new question with over length advice
    When a trainer enters the question edit page
    And Trainer add a new question that the "<advice>" is over length
    Then Error message "The advice is over length" appears and stay at the same page

  @developing
  Scenario: Adding a new question with same description
    When a trainer enters the question edit page
    And Trainer add a new question that the "<description>" is the same with an existing question
    Then Error message "The question already exists" appears and stay at the same page

  @developing
  Scenario: trainer add a question with only 1 option
    When a trainer enters the question edit page
    And Add a question that has only 1 option
    Then Error message appears and stay at the same page

  @developing
  Scenario: trainer add a question with 5 options
    When a trainer enters the question edit page
    And Add a question that has 5 options
    Then Add the question in the question list

  @developing
  Scenario: correct answer text is nothing
    When a trainer enters the question edit page
    And Add a question that has correct answer text is nothing
    Then Error message appears and stay at the same page

  @developing
  Scenario: trainer add a question with an empty description
    When a trainer enters the question edit page
    And trainer add a question
    And "<description>" is empty
    Then Error message appears and stay at the same page

  @developing
  Scenario: trainer add a question with empty options
    When a trainer enters the question edit page
    And trainer add a question
    And "<option>" is empty
    Then Error message appears and stay at the same page

  @developing
  Scenario: trainer add a question with empty options
    When a trainer enters the question edit page
    And trainer add a question
    And "<advise>" is empty
    Then Error message appears and stay at the same page

  @developing
  Scenario: trainer add a question without selecting the correct answer
    When a trainer enters the question edit page
    And trainer add a question
    And  none of options is selected as correct answer
    Then Error message appears and stay at the same page

  @developing
  Scenario: trainer add a question that has skipped option lines (option 1 & 3)
    Given a trainer enters the question edit page
    When he add a question description "what is 2*2?"
    And add question option1 "3."
    And add question option3 "4."
    And he set the option 3 as the correct answer
    And he set the advise "you should read a math book"
    And he press the "<add_button>"
    Then he see the message "Error. Blank option must not be included before the bottom option" and stay at the same page

  @developing
  Scenario: trainer add a question that has skipped option lines (only option 3)
    Given a trainer enters the question edit page
    When he add a question description "what is 2*2?"
    And add question option3 "4."
    And he set the option 3 as the correct answer
    And he set the advise "you should read a math book"
    And he press the "<add_button>"
    Then he see the message "Error. Blank option must not be included before the bottom option" and stay at the same page

  @developing
  Scenario: trainer add a question that has skipped option lines (option 1 & 4. 2 blank lines)
    Given a trainer enters the question edit page
    When he add a question description "what is 2*2?"
    And add question option1 "1."
    And add question option4 "of course 4."
    And he set the option 4 as the correct answer
    And he set the advise "you should read a math book"
    And he press the "<add_button>"
    Then he see the message "Error. Blank option must not be included before the bottom option" and stay at the same page