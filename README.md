# Hotel Data Aggregator

This application convert aggregated data from XML and JSON files to a single JSON object.
Additionally, the application extracts images from the result JSON object.

The input dataset must be located in the folder: _src/main/resources/storage_.
Merge the XML and JSON files into a single JSON object use comparator to avoid duplicates based on _giata_id_ and _GiataID_.

At the time of conversion, the application saves the image files to folder _src/main/resources/images_ for files of type _Coah_.
### Storage

The application uses file storage:

- /storage - source xml and json files, which should be aggregated to a single json object
- /storage/result - single Json object, which is created from xml and json files
- /storage/result/images - store images, which are extracted from the result should be stored in subfolder with result id

**Note:** If you run the application from Intellij Idea, you can find these storages in _build/resources/main_.

### Build and Run

This application is built with Gradle.
The application can be run on Java 11 or higher.

To build the application, run the following command:

```bash
./gradlew clean build
```

To run the application, run the following command:

```bash
./gradlew bootRun
```

## Rest API
### Init aggregation of XML and JSON files to a single JSON object:

&nbsp;&nbsp; **URL** : `/api/aggregate`

&nbsp;&nbsp; **Method** : `POST`

&nbsp;&nbsp; **Body** : Empty

&nbsp;&nbsp; **Response Code** : 202

&nbsp;&nbsp; **Response Body** :
```json
    {
        "resultId": "8fcba3a6-1da4-4aea-bbf8-719e3e6c0ffb"
    }
```

### Get aggregated JSON object by Result ID:

&nbsp;&nbsp; **URL** : `/api/json/{id}`

&nbsp;&nbsp; id - this is the resultId from the aggregation request

&nbsp;&nbsp; **Method** : `GET`

&nbsp;&nbsp; **Response Body** :
```json
    {
        // JSON Array
    }
```

### Get Result JSON object from CLI:

```bash
curl -s http://localhost:8080/api/json/8fcba3a6-1da4-4aea-bbf8-719e3e6c0ffb | jq
```
