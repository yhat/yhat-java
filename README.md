#yhat-java

A Yhat ScienceOps client written in Java for calling R and Python analytical routines deployed to ScienceOps.

## Including in your project

A JAR file of the `yhatclient` package can be downloaded from.

https://github.com/yhat/yhat-java/raw/master/target/YhatClient-1.0-SNAPSHOT.jar

TODO:

Upload to the central maven repository.

## Example

To call a [hello world model](https://docs.yhathq.com/python/examples/hello-world) deployed to Yhat ScienceOps.

```java
package mypackage;

import com.yhathq.yhatclient.YhatClient;

public class YhatClientExample {
    public static void main(Strings[] args) {
        String username = "MY_USERNAME";
        String apikey = "MY_APIKEY";
        String hostname = "http://cloud.yhathq.com/";
        String modelname = "HelloWorld";
        try {
            System.out.println("Making prediction through your model:");
            YhatClient yhat = new YhatClient(username, apikey, hostname);
            String rawdata = "{\"name\": \"Barbara\"}";
            System.out.println(yhat.predictRaw(rawdata, modelname));
        } catch (Exception e) {
            System.err.println("Something went horribly wrong");
        }
    }
}
```

Output:
```
Making prediction through your model:
{
  "result": {
    "greeting": "Hello Barbara!"
  },
  "yhat_id": "ea8d278d-ad5b-484a-bf0c-74e575b451c4",
  "yhat_model": "HelloWorld"
}
```
