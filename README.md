# AutoValue: Map Extension

An extension for Google's [AutoValue](https://github.com/google/auto) that creates methods for converting to/from AutoValue models and `Map<String, Object>`.

## Usage

Include auto-value-map in your project and add a `toMap()` method to your AutoValue object.

```java
@AutoValue
public abstract class Person {
  public abstract String name();
  public abstract int age();

  //toMap() will be replaced
  public abstract Map<String, Object> toMap();

  //fromMap() is static so you need to create a method to call it.
  public static Person create(Map<String, Object> map) {
    return AutoValue_Person.fromMap(map);
  }
}
```

## Types

Primitives, boxed primitives, strings, primtive arrays and enums (via `@MapEnum`) are supported by default. To support any other types,
such as Integer[] you need to use a `MapAdapter`.

Full list of supported types:

 * `byte`, `Byte`, `byte[]`
 * `int`, `Integer`, `int[]`
 * `long`, `Long`, `long[]`
 * `short`, `Short`, `Short[]`
 * `boolean`, `Boolean`, `boolean[]`
 * `float`, `Float`, `float[]`
 * `double`, `Double`, `double[]`
 * `char`, `Char`, `char[]`
 * `String`
 * `Enum` (via `@MapEnum`)

 ### Annotations

 **MapEnum**
 Automatically converts enums to/from strings using name() and valueOf()

 **MapHide**
 Prevents an element from being included the map, by default when creating an object from a
 map null be set for nullable hidden elements and 0 or false for primitive or non-nullable boxed primitives.

 **MapAdapter**
 Provide an adapter for a element, this can replace the built in conversion for supported types as well.
 The adapter must be specified and the `mapType` must be set if not string.
 The `mapType` is the type for the data in the map. The adapter must convert from and to the method type and the map type.

 **MapElementName**
 By default the map key is the method name this can be changed using `@MapElementName`

 Example:

```java
@AutoValue
public abstract class Car {
  public enum Colour {
    BLUE, RED, GREEN
  }

  @MapEnum
  public abstract Colour colour();
  @MapElementAdapter(adapter = StringListAdapter.class, mapType = String.class)
  public abstract List<String> owners();
  @MapHide
  public abstract Integer nullByDefault();
  @MapHide(SixMaker.class)
  public abstract Integer sixByDefault();
  @MapHide(value = SixMaker.class, readFromMap = true)
  public abstract Integer sixByDefaultUnlessMapContainsValue();
  @MapElementName("ADifferentName")
  public abstract String notThisName();
  ...
}

public class SixMaker implements MapValueMaker<Integer> {
  @Override
  public Integer make(String methodName) {
    return 6;
  }
}

public static class StringListAdapter implements MapAdapter<List<String>, String> {
  Gson gson = new Gson();
  Type typeToken = new TypeToken<List<String>>(){}.getType();

  @Override
  public List<String> fromMap(String methodName, String str) {
    return gson.fromJson(str, typeToken);
  }

  @Override
  public String toMap(String methodName, List<String> list) {
    return gson.toJson(list, typeToken);
  }
}
```

## Download

Add a Gradle dependency:

```groovy
annotationProcessor 'com.raybritton.autovaluemap:auto-value-map:2.2.0'
provided 'com.raybritton.autovaluemap:auto-value-map:2.2.0'

// Optional needed if you wanted to use @MapHide or @MapElementAdapter
compile 'com.raybritton.autovaluemap:annotations:2.2.0'

// Optional list adapters for String, Integer, Double and boolean
compile 'com.raybritton.autovaluemap.gson:gson-parsers:2.2.0'
```

## License

Many thanks to [Gabriel Ittner](https://github.com/gabrielittner/) (for [AutoValueCursor](https://github.com/gabrielittner/auto-value-cursor/) and in turn [Ryan Harter](https://github.com/rharter/) (for [AutoValueGson](https://github.com/rharter/auto-value-gson))

```
Copyright 2017 Ray Britton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
