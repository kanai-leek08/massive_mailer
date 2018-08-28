Feature: Add Question
  As a trainer, I want to add new questions,
  so that students can take test.

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
  Scenario: trainer add a question with 2 options
    When a trainer enters the question edit page
    And Add a question that has 2 options
    Then Add the question in the question list

  @developing
  Scenario: trainer add a question with 5 options
    When a trainer enters the question edit page
    And Add a question that has 5 options
    Then Add the question in the question list

  @developing
  Scenario: trainer add a question with 6 options
    When a trainer enters the question edit page
    And Add a question that has 6 options
    Then Error message appears and stay at the same page

  @developing
  Scenario: correct answer text is nothing
    When a trainer enters the question edit page
    And Add a question that has correct answer text is nothing
    Then Error message appears and stay at the same page

  @developing
  Scenario: trainer add a option
    When a trainer enters the question edit page
    And I add option
    And Push "+(Add option)" button
    And Currently number of option is 4
    Then Add to new option input field to same page

  @developing
  Scenario: trainer add a option
    When a trainer enters the question edit page
    And I add option
    And Push "+(Add option)" button
    And Currently number of option is 5
    Then Error message appears and stay at the same page

  @developing
  Scenario: trainer minus a option
    When a trainer enters the question edit page
    And I minus option
    And Push "-(Minus option)" button
    And Minus button is place to each option's right side
    And Currently number of option is 3
    Then Minus to specified option input field to same page

  @developing
  Scenario: trainer minus a option
    When a trainer enters the question edit page
    And I minus option
    And Push "-(Minus option)" button
    And Minus button is place to each option's right side
    And Currently number of option is 2
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
