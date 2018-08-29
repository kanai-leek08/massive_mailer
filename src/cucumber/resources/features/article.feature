Feature: 記事

  @now
  Scenario: 記事を登録する
    Given 記事登録ページに遷移
    When 記事情報を入力してサブミット
        |title|ゴールデンレトリバーがうちにきた!                     |
        |body |2022/12/11 ついに念願のゴールデンがうちに。名前は・・・|
    Then 記事一覧ページに"ゴールデンレトリバーがうちにきた!"が表示されている

   @now
   Scenario Outline:
    Given 記事登録ページに遷移
    When 記事情報のフォーム<"column"> に 不正な <"value"> を入力してサブミット
    Then "<error message>"が表示されている

    Examples:
        |column  |value|error message          |
        |title   |     |タイトルを入力してください |
        |article |     |本文を入力してください    |

  @now
  Scenario: 記事登録エラー
    Given 記事登録ページに遷移
    When 記事情報を入力してサブミット
        |title|ゴールデンレトリバーがうちにきた!|
        |body |                             |
    Then "本文を入力してください"が表示されている

  @now
  Scenario: 記事登録エラー
    Given 記事登録ページに遷移
    When 記事情報を入力してサブミット
        |title||
        |body |xxx|
    Then "タイトルを入力してください"が表示されている

  @now
  Scenario: 記事一覧が表示される
    Given 記事が"2"件ある
    When 記事一覧ページに遷移
    Then 記事が"2"件が表示されている
