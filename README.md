# Dramaメモアプリ
RaiseTech 22年06月度 最終課題Spring Boot RestAPIのCRUDアプリケーション

---
## 概要
見たいドラマを忘れないよう、メモしておけるCRUDアプリです。  
READ・CREATE・UPDATE・DELETE機能を実装予定。  
単体テスト、結合テスト、GitHub ActionsでのCIも実装予定。

---
## 構成要件
* Java 17
* Spring Boot 3.1.0
* （JUnit 5.8.2）※使用予定
* MySQL 8.0.33
* Docker 20.10.21
* （JUnit 5）※使用予定
* （Mockito）※使用予定

---
## 機能一覧

| 機能 | 詳細 | URL |
| ------------ | ------------- | ------------- |
| 全件取得 | ドラマ一覧を返す | /drama |
| 絞り込み検索 | priorityカラムの｛A、B、C｝を指定して検索 | /drama?priority=○○ |
| 新規登録 | 新しいタイトルを登録する | /drama |
| リストの編集 | ※実装予定 |  |
| 削除 | ※実装予定 |  |


---

## DBテーブル
テーブル名：drama  

| カラム名 | データ型 | NotNull | 備考 |
| ------------ | ------------- | ------------- | ------------- | 
| id | int | NOT NULL | ID、主キー、自動生成 |
| title | VARCHAR(100) | NOT NULL | 見たいドラマのタイトル |
| year | VARCHAR(100)  |  | 発表年 |
| priority | VARCHAR(1) | NOT NULL | A>B>Cの順で優先順位が高いとする |

---

## 実行結果
<details>
<summary><h4> 1-1. GET /drama 一覧表示 </h4></summary>

![](imgs/2023-06-29-14-45-06.png)
</details>
<details>
<summary><h4> 1-1. GET /drama?priority=○ 優先度で絞り込み </h4></summary>

![](imgs/2023-06-29-14-47-38.png)
</details>
<details>
<summary><h4> 1-1. GET /drama?priority=○ priorityに存在しないものを指定したとき </h4></summary>

![](imgs/2023-06-29-14-50-08.png)
</details>
<details>
<summary><h4> 2-1. POST /drama 新規登録</h4></summary>
<h5>ResponseBody</h5>

![](imgs/2023-06-29-14-35-02.png)
<h5>ResponseHeader</h5>

![](imgs/2023-06-29-14-35-52.png)
</details>
<details>
<summary><h4> 2-2. POST /drama バリデーションエラー時</h4></summary>

![](imgs/2023-06-29-14-41-28.png)
</details>

