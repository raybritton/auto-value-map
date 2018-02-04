package com.raybritton.autovaluemap;

import com.google.auto.value.processor.AutoValueProcessor;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class AutoValueMapExtensionTest {

    @Test
    public void simple() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", "" +
                "package test;\n" +
                "import java.util.Map;\n" +
                "import java.lang.String;\n" +
                "import java.lang.Long;\n" +
                "import com.google.auto.value.AutoValue;\n" +
                "@AutoValue\n" +
                "public abstract class Test {\n" +
                "public abstract String str();\n" +
                "public abstract Long clong();\n" +
                "public abstract long plong();\n" +
                "public abstract Map<String, Object> toMap();\n" +
                "}");
        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", "" +
                "package test;\n" +
                "\n" +
                "import java.lang.Long;\n" +
                "import java.lang.Object;\n" +
                "import java.lang.String;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "final class AutoValue_Test extends $AutoValue_Test {\n" +
                "  AutoValue_Test(String str, Long clong, long plong) {\n" +
                "    super(str, clong, plong);\n" +
                "  }\n" +
                "\n" +
                "  public Map<String, Object> toMap() {\n" +
                "    Map<String, Object> map = new HashMap<String, Object>();\n" +
                "    map.put(\"str\", str());\n" +
                "    map.put(\"clong\", clong());\n" +
                "    map.put(\"plong\", plong());\n" +
                "    return map;\n" +
                "  }\n" +
                "\n" +
                "  public static Test fromMap(Map<String, Object> map) {\n" +
                "    String str = (String) map.get(\"str\");\n" +
                "    Long clong = (Long) map.get(\"clong\");\n" +
                "    long plong = (long) map.get(\"plong\");\n" +
                "    return new AutoValue_Test(str, clong, plong);\n" +
                "  }\n" +
                "}"
        );

        JavaFileObject rootExpected = JavaFileObjects.forSourceString("test.$AutoValue_Test", "" +
                "package test;\n" +
                "\n" +
                "import javax.annotation.Generated;\n" +
                "\n" +
                "@Generated(\"com.google.auto.value.processor.AutoValueProcessor\")\n" +
                "abstract class $AutoValue_Test extends Test {\n" +
                "\n" +
                "  private final String str;\n" +
                "  private final Long clong;\n" +
                "  private final long plong;\n" +
                "\n" +
                "  $AutoValue_Test(\n" +
                "      String str,\n" +
                "      Long clong,\n" +
                "      long plong) {\n" +
                "    if (str == null) {\n" +
                "      throw new NullPointerException(\"Null str\");\n" +
                "    }\n" +
                "    this.str = str;\n" +
                "    if (clong == null) {\n" +
                "      throw new NullPointerException(\"Null clong\");\n" +
                "    }\n" +
                "    this.clong = clong;\n" +
                "    this.plong = plong;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public String str() {\n" +
                "    return str;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public Long clong() {\n" +
                "    return clong;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public long plong() {\n" +
                "    return plong;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public String toString() {\n" +
                "    return \"Test{\"\n" +
                "        + \"str=\" + str + \", \"\n" +
                "        + \"clong=\" + clong + \", \"\n" +
                "        + \"plong=\" + plong\n" +
                "        + \"}\";\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public boolean equals(Object o) {\n" +
                "    if (o == this) {\n" +
                "      return true;\n" +
                "    }\n" +
                "    if (o instanceof Test) {\n" +
                "      Test that = (Test) o;\n" +
                "      return (this.str.equals(that.str()))\n" +
                "           && (this.clong.equals(that.clong()))\n" +
                "           && (this.plong == that.plong());\n" +
                "    }\n" +
                "    return false;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public int hashCode() {\n" +
                "    int h = 1;\n" +
                "    h *= 1000003;\n" +
                "    h ^= this.str.hashCode();\n" +
                "    h *= 1000003;\n" +
                "    h ^= this.clong.hashCode();\n" +
                "    h *= 1000003;\n" +
                "    h ^= (int) ((this.plong >>> 32) ^ this.plong);\n" +
                "    return h;\n" +
                "  }" +
                "}"
        );

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor(Collections.singletonList(new AutoValueMapExtension())))
                .compilesWithoutError()
                .and()
                .generatesSources(expected, rootExpected);
    }

    @Test
    public void doesNotExecute() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", "" +
                "package test;\n" +
                "import java.lang.String;\n" +
                "import com.google.auto.value.AutoValue;\n" +
                "@AutoValue\n" +
                "public abstract class Test {\n" +
                "public abstract String str();\n" +
                "}");
        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", "" +
                "package test;\n" +
                "\n" +
                "import javax.annotation.Generated;\n" +
                "\n" +
                "@Generated(\"com.google.auto.value.processor.AutoValueProcessor\")\n" +
                "\n" +
                "final class AutoValue_Test extends Test {\n" +
                "\n" +
                "  private final String str;\n" +
                "\n" +
                "  AutoValue_Test(\n" +
                "      String str) {\n" +
                "    if (str == null) {\n" +
                "      throw new NullPointerException(\"Null str\");\n" +
                "    }\n" +
                "    this.str = str;\n" +
                "  }\n" +
                "\n" +
                "@Override\n" +
                "  public String str() {\n" +
                "    return str;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public String toString() {\n" +
                "    return \"Test{\"\n" +
                "        + \"str=\" + str\n" +
                "        + \"}\";\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public boolean equals(Object o) {\n" +
                "    if (o == this) {\n" +
                "      return true;\n" +
                "    }\n" +
                "    if (o instanceof Test) {\n" +
                "      Test that = (Test) o;\n" +
                "      return (this.str.equals(that.str()));\n" +
                "    }\n" +
                "    return false;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public int hashCode() {\n" +
                "    int h = 1;\n" +
                "    h *= 1000003;\n" +
                "    h ^= this.str.hashCode();\n" +
                "    return h;\n" +
                "  }" +
                "}");

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor(Collections.singletonList(new AutoValueMapExtension())))
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void extEnumTest() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Example", "" +
                "package test;\n" +
                "import test.Status;\n" +
                "import java.util.Map;\n" +
                "import com.raybritton.autovaluemap.annotations.MapEnum;\n" +
                "import com.google.auto.value.AutoValue;\n" +
                "@AutoValue\n" +
                "public abstract class Example {\n" +
                "    public abstract String webId();\n" +
                "    public abstract Long issuedAt();\n" +
                "    public abstract String issuedByUserId();\n" +
                "    public abstract Long observedFrom();\n" +
                "    public abstract Long observedTo();\n" +
                "    public abstract String spoilReason();\n" +
                "    @MapEnum\n" +
                "    public abstract Status status();\n" +
                "    public abstract String street();\n" +
                "    public abstract String photo1File();\n" +
                "    public abstract String photo2File();\n" +
                "    public abstract String photo3File();\n" +
                "    public abstract String photo4File();\n" +
                "    public abstract Map<String, Object> toMap();\n" +
                "}");
        JavaFileObject enumSource = JavaFileObjects.forSourceString("test.Status", "" +
                "package test;\n" +
                "public enum Status {\n" +
                "    UNISSUED, INCOMPLETE, ISSUED, SPOILED\n" +
                "}");
        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Example", "" +
                "package test;\n" +
                "\n" +
                "import java.lang.Long;\n" +
                "import java.lang.Object;\n" +
                "import java.lang.String;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "final class AutoValue_Example extends $AutoValue_Example {\n" +
                "  AutoValue_Example(String webId, Long issuedAt, String issuedByUserId, Long observedFrom, Long observedTo, String spoilReason, Status status, String street, String photo1File, String photo2File, String photo3File, String photo4File) {\n" +
                "    super(webId, issuedAt, issuedByUserId, observedFrom, observedTo, spoilReason, status, street, photo1File, photo2File, photo3File, photo4File);\n" +
                "  }\n" +
                "\n" +
                "  public Map<String, Object> toMap() {\n" +
                "    Map<String, Object> map = new HashMap<String, Object>();\n" +
                "    map.put(\"webId\", webId());\n" +
                "    map.put(\"issuedAt\", issuedAt());\n" +
                "    map.put(\"issuedByUserId\", issuedByUserId());\n" +
                "    map.put(\"observedFrom\", observedFrom());\n" +
                "    map.put(\"observedTo\", observedTo());\n" +
                "    map.put(\"spoilReason\", spoilReason());\n" +
                "    if (status() != null) {\n" +
                "      map.put(\"status\", status().name());\n" +
                "    }\n" +
                "    map.put(\"street\", street());\n" +
                "    map.put(\"photo1File\", photo1File());\n" +
                "    map.put(\"photo2File\", photo2File());\n" +
                "    map.put(\"photo3File\", photo3File());\n" +
                "    map.put(\"photo4File\", photo4File());\n" +
                "    return map;\n" +
                "  }\n" +
                "\n" +
                "  public static Example fromMap(Map<String, Object> map) {\n" +
                "    String webId = (String) map.get(\"webId\");\n" +
                "    Long issuedAt = (Long) map.get(\"issuedAt\");\n" +
                "    String issuedByUserId = (String) map.get(\"issuedByUserId\");\n" +
                "    Long observedFrom = (Long) map.get(\"observedFrom\");\n" +
                "    Long observedTo = (Long) map.get(\"observedTo\");\n" +
                "    String spoilReason = (String) map.get(\"spoilReason\");\n" +
                "    Status status = null\n" +
                "    if (map.get(\"status\") != null) {\n" +
                "      status = Enum.valueOf(Status.class, map.get(\"status\").toString());\n" +
                "    }\n" +
                "    String street = (String) map.get(\"street\");\n" +
                "    String photo1File = (String) map.get(\"photo1File\");\n" +
                "    String photo2File = (String) map.get(\"photo2File\");\n" +
                "    String photo3File = (String) map.get(\"photo3File\");\n" +
                "    String photo4File = (String) map.get(\"photo4File\");\n" +
                "    return new AutoValue_Example(webId, issuedAt, issuedByUserId, observedFrom, observedTo, spoilReason, status, street, photo1File, photo2File, photo3File, photo4File);\n" +
                "  }\n" +
                "}");

        assertAbout(javaSources())
                .that(Arrays.asList(enumSource, source))
                .processedWith(new AutoValueProcessor(Collections.singletonList(new AutoValueMapExtension())))
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void intEnumTest() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Example", "" +
                "package test;\n" +
                "import java.util.Map;\n" +
                "import com.raybritton.autovaluemap.annotations.MapEnum;\n" +
                "import com.raybritton.autovaluemap.annotations.MapHide;\n" +
                "import com.raybritton.autovaluemap.Nullable;\n" +
                "import com.google.auto.value.AutoValue;\n" +
                "@AutoValue\n" +
                "public abstract class Example {\n" +
                "public enum Status {\n" +
                "UNISSUED, INCOMPLETE, ISSUED, SPOILED\n" +
                "}\n" +
                "    public abstract String webId();\n" +
                "    public abstract Long issuedAt();\n" +
                "    public abstract String issuedByUserId();\n" +
                "    @MapHide\n" +
                "    public abstract Long observedFrom();\n" +
                "    @Nullable\n" +
                "    @MapHide\n" +
                "    public abstract Long observedTo();\n" +
                "    public abstract String spoilReason();\n" +
                "    @MapEnum\n" +
                "    public abstract Status status();\n" +
                "    public abstract String street();\n" +
                "    public abstract String photo1File();\n" +
                "    public abstract String photo2File();\n" +
                "    public abstract String photo3File();\n" +
                "    public abstract String photo4File();\n" +
                "    public abstract Map<String, Object> toMap();\n" +
                "}");
        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Example", "" +
                "package test;\n" +
                "\n" +
                "import com.raybritton.autovaluemap.makers.LongMaker;\n" +
                "import com.raybritton.autovaluemap.makers.NullMaker;\n" +
                "import java.lang.Long;\n" +
                "import java.lang.Object;\n" +
                "import java.lang.String;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "final class AutoValue_Example extends $AutoValue_Example {\n" +
                "  AutoValue_Example(String webId, Long issuedAt, String issuedByUserId, Long observedFrom, Long observedTo, String spoilReason, Example.Status status, String street, String photo1File, String photo2File, String photo3File, String photo4File) {\n" +
                "    super(webId, issuedAt, issuedByUserId, observedFrom, observedTo, spoilReason, status, street, photo1File, photo2File, photo3File, photo4File);\n" +
                "  }\n" +
                "\n" +
                "  public Map<String, Object> toMap() {\n" +
                "    Map<String, Object> map = new HashMap<String, Object>();\n" +
                "    map.put(\"webId\", webId());\n" +
                "    map.put(\"issuedAt\", issuedAt());\n" +
                "    map.put(\"issuedByUserId\", issuedByUserId());\n" +
                "    map.put(\"spoilReason\", spoilReason());\n" +
                "    if (status() != null) {\n" +
                "      map.put(\"status\", status().name());\n" +
                "    }\n" +
                "    map.put(\"street\", street());\n" +
                "    map.put(\"photo1File\", photo1File());\n" +
                "    map.put(\"photo2File\", photo2File());\n" +
                "    map.put(\"photo3File\", photo3File());\n" +
                "    map.put(\"photo4File\", photo4File());\n" +
                "    return map;\n" +
                "  }\n" +
                "\n" +
                "  public static Example fromMap(Map<String, Object> map) {\n" +
                "    String webId = (String) map.get(\"webId\");\n" +
                "    Long issuedAt = (Long) map.get(\"issuedAt\");\n" +
                "    String issuedByUserId = (String) map.get(\"issuedByUserId\");\n" +
                "    Long observedFrom;\n" +
                "    try {\n" +
                "      LongMaker observedFromMaker = (LongMaker) LongMaker.class.newInstance();\n" +
                "      observedFrom = (Long) observedFromMaker.make(\"observedFrom\");\n" +
                "    } catch (InstantiationException | IllegalAccessException e) {\n" +
                "      throw new IllegalStateException(\"Cannot instantiate LongMaker for observedFrom because \"+e.getMessage());\n" +
                "    }\n" +
                "    Long observedTo;\n" +
                "    try {\n" +
                "      NullMaker observedToMaker = (NullMaker) NullMaker.class.newInstance();\n" +
                "      observedTo = (Long) observedToMaker.make(\"observedTo\");\n" +
                "    } catch (InstantiationException | IllegalAccessException e) {\n" +
                "      throw new IllegalStateException(\"Cannot instantiate NullMaker for observedTo because \"+e.getMessage());\n" +
                "    }\n" +
                "    String spoilReason = (String) map.get(\"spoilReason\");\n" +
                "    Example.Status status = null;\n" +
                "    if (map.get(\"status\") != null) {\n" +
                "      status = Enum.valueOf(Example.Status.class, map.get(\"status\").toString());\n" +
                "    }\n" +
                "    String street = (String) map.get(\"street\");\n" +
                "    String photo1File = (String) map.get(\"photo1File\");\n" +
                "    String photo2File = (String) map.get(\"photo2File\");\n" +
                "    String photo3File = (String) map.get(\"photo3File\");\n" +
                "    String photo4File = (String) map.get(\"photo4File\");\n" +
                "    return new AutoValue_Example(webId, issuedAt, issuedByUserId, observedFrom, observedTo, spoilReason, status, street, photo1File, photo2File, photo3File, photo4File);\n" +
                "  }\n" +
                "}");

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor(Collections.singletonList(new AutoValueMapExtension())))
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void customNames() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", "" +
                "package test;\n" +
                "import java.util.Map;\n" +
                "import java.lang.String;\n" +
                "import java.lang.Long;\n" +
                "import com.google.auto.value.AutoValue;\n" +
                "import com.raybritton.autovaluemap.annotations.MapElementName;" +
                "@AutoValue\n" +
                "public abstract class Test {\n" +
                "@MapElementName(\"notAString\")\n" +
                "public abstract String str();\n" +
                "public abstract Long clong();\n" +
                "public abstract long plong();\n" +
                "public abstract Map<String, Object> toMap();\n" +
                "}");
        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", "" +
                "package test;\n" +
                "\n" +
                "import java.lang.Long;\n" +
                "import java.lang.Object;\n" +
                "import java.lang.String;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "final class AutoValue_Test extends $AutoValue_Test {\n" +
                "  AutoValue_Test(String str, Long clong, long plong) {\n" +
                "    super(str, clong, plong);\n" +
                "  }\n" +
                "\n" +
                "  public Map<String, Object> toMap() {\n" +
                "    Map<String, Object> map = new HashMap<String, Object>();\n" +
                "    map.put(\"notAString\", str());\n" +
                "    map.put(\"clong\", clong());\n" +
                "    map.put(\"plong\", plong());\n" +
                "    return map;\n" +
                "  }\n" +
                "\n" +
                "  public static Test fromMap(Map<String, Object> map) {\n" +
                "    String str = (String) map.get(\"notAString\");\n" +
                "    Long clong = (Long) map.get(\"clong\");\n" +
                "    long plong = (long) map.get(\"plong\");\n" +
                "    return new AutoValue_Test(str, clong, plong);\n" +
                "  }\n" +
                "}"
        );

        JavaFileObject rootExpected = JavaFileObjects.forSourceString("test.$AutoValue_Test", "" +
                "package test;\n" +
                "\n" +
                "import com.raybritton.autovaluemap.annotations.MapElementName;\n" +
                "import javax.annotation.Generated;\n" +
                "\n" +
                "@Generated(\"com.google.auto.value.processor.AutoValueProcessor\")\n" +
                "abstract class $AutoValue_Test extends Test {\n" +
                "\n" +
                "  private final String str;\n" +
                "  private final Long clong;\n" +
                "  private final long plong;\n" +
                "\n" +
                "  $AutoValue_Test(\n" +
                "      String str,\n" +
                "      Long clong,\n" +
                "      long plong) {\n" +
                "    if (str == null) {\n" +
                "      throw new NullPointerException(\"Null str\");\n" +
                "    }\n" +
                "    this.str = str;\n" +
                "    if (clong == null) {\n" +
                "      throw new NullPointerException(\"Null clong\");\n" +
                "    }\n" +
                "    this.clong = clong;\n" +
                "    this.plong = plong;\n" +
                "  }\n" +
                "\n" +
                "  @MapElementName(value = \"notAString\")\n" +
                "  @Override\n" +
                "  public String str() {\n" +
                "    return str;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public Long clong() {\n" +
                "    return clong;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public long plong() {\n" +
                "    return plong;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public String toString() {\n" +
                "    return \"Test{\"\n" +
                "        + \"str=\" + str + \", \"\n" +
                "        + \"clong=\" + clong + \", \"\n" +
                "        + \"plong=\" + plong\n" +
                "        + \"}\";\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public boolean equals(Object o) {\n" +
                "    if (o == this) {\n" +
                "      return true;\n" +
                "    }\n" +
                "    if (o instanceof Test) {\n" +
                "      Test that = (Test) o;\n" +
                "      return (this.str.equals(that.str()))\n" +
                "           && (this.clong.equals(that.clong()))\n" +
                "           && (this.plong == that.plong());\n" +
                "    }\n" +
                "    return false;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public int hashCode() {\n" +
                "    int h = 1;\n" +
                "    h *= 1000003;\n" +
                "    h ^= this.str.hashCode();\n" +
                "    h *= 1000003;\n" +
                "    h ^= this.clong.hashCode();\n" +
                "    h *= 1000003;\n" +
                "    h ^= (int) ((this.plong >>> 32) ^ this.plong);\n" +
                "    return h;\n" +
                "  }" +
                "}"
        );

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor(Collections.singletonList(new AutoValueMapExtension())))
                .compilesWithoutError()
                .and()
                .generatesSources(expected, rootExpected);
    }

    @Test
    public void customAdapter() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", "" +
                "package test;\n" +
                "import java.util.Map;\n" +
                "import java.lang.String;\n" +
                "import java.lang.Long;\n" +
                "import com.google.auto.value.AutoValue;\n" +
                "import com.raybritton.autovaluemap.annotations.MapElementAdapter;\n" +
                "import com.raybritton.autovaluemap.StringLongAdapter;\n" +
                "@AutoValue\n" +
                "public abstract class Test {\n" +
                "public abstract String str();\n" +
                "@MapElementAdapter(adapter=StringLongAdapter.class, mapType=String.class)\n" +
                "public abstract Long clong();\n" +
                "public abstract long plong();\n" +
                "public abstract Map<String, Object> toMap();\n" +
                "}");
        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", "" +
                "package test;\n" +
                "\n" +
                "import com.raybritton.autovaluemap.MapAdapter;\n" +
                "import com.raybritton.autovaluemap.StringLongAdapter;\n" +
                "import java.lang.Long;\n" +
                "import java.lang.Object;\n" +
                "import java.lang.String;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "final class AutoValue_Test extends $AutoValue_Test {\n" +
                "  AutoValue_Test(String str, Long clong, long plong) {\n" +
                "    super(str, clong, plong);\n" +
                "  }\n" +
                "\n" +
                "  public Map<String, Object> toMap() {\n" +
                "    Map<String, Object> map = new HashMap<String, Object>();\n" +
                "    map.put(\"str\", str());\n" +
                "    try {\n" +
                "      MapAdapter adapter = (MapAdapter) StringLongAdapter.class.newInstance();\n" +
                "      map.put(\"clong\", (String) adapter.toMap(\"clong\", clong()));\n" +
                "    } catch (InstantiationException | IllegalAccessException e) {\n" +
                "      throw new IllegalStateException(\"Cannot instantiate adapter for clong because \"+e.getMessage())\", element.getMethodName());\n" +
                "    } \n" +
                "    map.put(\"plong\", plong());\n" +
                "    return map;\n" +
                "  }\n" +
                "\n" +
                "  public static Test fromMap(Map<String, Object> map) {\n" +
                "    String str = (String) map.get(\"str\");\n" +
                "    Long clong;\n" +
                "    try {\n" +
                "      MapAdapter adapter = (MapAdapter) StringLongAdapter.class.newInstance();\n" +
                "      clong = (Long) adapter.fromMap(\"clong\", (String) map.get(\"clong\"));\n" +
                "    } catch (InstantiationException | IllegalAccessException e) {\n" +
                "      throw new IllegalStateException(\"Cannot instantiate adapter for clong because \"+e.getMessage());\n" +
                "    }\n" +
                "    long plong = (long) map.get(\"plong\");\n" +
                "    return new AutoValue_Test(str, clong, plong);\n" +
                "  }\n" +
                "}"
        );

        JavaFileObject rootExpected = JavaFileObjects.forSourceString("test.$AutoValue_Test", "" +
                "package test;\n" +
                "\n" +
                "import com.raybritton.autovaluemap.StringLongAdapter;\n" +
                "import com.raybritton.autovaluemap.annotations.MapElementAdapter;\n" +
                "import javax.annotation.Generated;\n" +
                "\n" +
                "@Generated(\"com.google.auto.value.processor.AutoValueProcessor\")\n" +
                "abstract class $AutoValue_Test extends Test {\n" +
                "\n" +
                "  private final String str;\n" +
                "  private final Long clong;\n" +
                "  private final long plong;\n" +
                "\n" +
                "  $AutoValue_Test(\n" +
                "      String str,\n" +
                "      Long clong,\n" +
                "      long plong) {\n" +
                "    if (str == null) {\n" +
                "      throw new NullPointerException(\"Null str\");\n" +
                "    }\n" +
                "    this.str = str;\n" +
                "    if (clong == null) {\n" +
                "      throw new NullPointerException(\"Null clong\");\n" +
                "    }\n" +
                "    this.clong = clong;\n" +
                "    this.plong = plong;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public String str() {\n" +
                "    return str;\n" +
                "  }\n" +
                "\n" +
                "  @MapElementAdapter(adapter = StringLongAdapter.class, mapType = String.class)\n" +
                "  @Override\n" +
                "  public Long clong() {\n" +
                "    return clong;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public long plong() {\n" +
                "    return plong;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public String toString() {\n" +
                "    return \"Test{\"\n" +
                "         + \"str=\" + str + \", \"\n" +
                "         + \"clong=\" + clong + \", \"\n" +
                "         + \"plong=\" + plong\n" +
                "        + \"}\";\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public boolean equals(Object o) {\n" +
                "    if (o == this) {\n" +
                "      return true;\n" +
                "    }\n" +
                "    if (o instanceof Test) {\n" +
                "      Test that = (Test) o;\n" +
                "      return (this.str.equals(that.str()))\n" +
                "           && (this.clong.equals(that.clong()))\n" +
                "           && (this.plong == that.plong());\n" +
                "    }\n" +
                "    return false;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public int hashCode() {\n" +
                "    int h = 1;\n" +
                "    h *= 1000003;\n" +
                "    h ^= this.str.hashCode();\n" +
                "    h *= 1000003;\n" +
                "    h ^= this.clong.hashCode();\n" +
                "    h *= 1000003;\n" +
                "    h ^= (int) ((this.plong >>> 32) ^ this.plong);\n" +
                "    return h;\n" +
                "  }\n" +
                "}"
        );

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor(Collections.singletonList(new AutoValueMapExtension())))
                .compilesWithoutError()
                .and()
                .generatesSources(expected, rootExpected);
    }

    @Test
    public void hiddenObjects() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", "" +
                "package test;\n" +
                "import java.util.Map;\n" +
                "import java.lang.String;\n" +
                "import java.lang.Long;\n" +
                "import com.google.auto.value.AutoValue;\n" +
                "import com.raybritton.autovaluemap.annotations.MapHide;\n" +
                "import com.raybritton.autovaluemap.makers.EmptyStringMaker;\n" +
                "@AutoValue\n" +
                "public abstract class Test {\n" +
                "@MapHide(EmptyStringMaker.class)\n" +
                "public abstract String str();\n" +
                "public abstract Map<String, Object> toMap();\n" +
                "}");
        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", "" +
                "package test;\n" +
                "\n" +
                "import com.raybritton.autovaluemap.makers.EmptyStringMaker;\n" +
                "import java.lang.Object;\n" +
                "import java.lang.String;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "final class AutoValue_Test extends $AutoValue_Test {\n" +
                "  AutoValue_Test(String str) {\n" +
                "    super(str);\n" +
                "  }\n" +
                "\n" +
                "  public Map<String, Object> toMap() {\n" +
                "    Map<String, Object> map = new HashMap<String, Object>();\n" +
                "    return map;\n" +
                "  }\n" +
                "\n" +
                "  public static Test fromMap(Map<String, Object> map) {\n" +
                "    String str;\n" +
                "    try {\n" +
                "      EmptyStringMaker strMaker = (EmptyStringMaker) EmptyStringMaker.class.newInstance();\n" +
                "      str = (String) strMaker.make(\"str\");\n" +
                "    } catch (InstantiationException | IllegalAccessException e) {\n" +
                "      throw new IllegalStateException(\"Cannot instantiate EmptyStringMaker for str because \"+e.getMessage());\n" +
                "    }\n" +
                "    return new AutoValue_Test(str);\n" +
                "  }\n" +
                "}"
        );

        JavaFileObject rootExpected = JavaFileObjects.forSourceString("test.$AutoValue_Test", "" +
                "package test;\n" +
                "\n" +
                "import com.raybritton.autovaluemap.makers.EmptyStringMaker;\n" +
                "import java.lang.Object;\n" +
                "import java.lang.String;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "final class AutoValue_Test extends $AutoValue_Test {\n" +
                "  AutoValue_Test(String str) {\n" +
                "    super(str);\n" +
                "  }\n" +
                "\n" +
                "  public Map<String, Object> toMap() {\n" +
                "    Map<String, Object> map = new HashMap<String, Object>();\n" +
                "    return map;\n" +
                "  }\n" +
                "\n" +
                "  public static Test fromMap(Map<String, Object> map) {\n" +
                "    String str;\n" +
                "    try {\n" +
                "      EmptyStringMaker strMaker = (EmptyStringMaker) EmptyStringMaker.class.newInstance();\n" +
                "      str = (String) strMaker.make(\"str\");\n" +
                "    } catch (InstantiationException | IllegalAccessException e) {\n" +
                "      throw new IllegalStateException(\"Cannot instantiate EmptyStringMaker for str because \"+e.getMessage());\n" +
                "    }\n" +
                "    return new AutoValue_Test(str);\n" +
                "  }\n" +
                "}"
        );

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor(Collections.singletonList(new AutoValueMapExtension())))
                .compilesWithoutError()
                .and()
                .generatesSources(expected, rootExpected);
    }

    @Test
    public void hiddenWithReadingObjects() {
        JavaFileObject source = JavaFileObjects.forSourceString("com.raybritton.autovaluemap.Test", "" +
                "package com.raybritton.autovaluemap;\n" +
                "import java.util.Map;\n" +
                "import java.lang.String;\n" +
                "import java.lang.Long;\n" +
                "import com.raybritton.autovaluemap.Nullable;\n" +
                "import com.google.auto.value.AutoValue;\n" +
                "import com.raybritton.autovaluemap.annotations.MapHide;" +
                "import com.raybritton.autovaluemap.makers.NullMaker;" +
                "@AutoValue\n" +
                "public abstract class Test {\n" +
                "@Nullable\n" +
                "@MapHide(value = NullMaker.class, readFromMap = true)\n" +
                "public abstract String str();\n" +
                "public abstract Map<String, Object> toMap();\n" +
                "}");
        JavaFileObject expected = JavaFileObjects.forSourceString("com.raybritton.autovaluemap.AutoValue_Test", "" +
                "package com.raybritton.autovaluemap;\n" +
                "\n" +
                "import com.raybritton.autovaluemap.makers.NullMaker;\n" +
                "import java.lang.Object;\n" +
                "import java.lang.String;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "final class AutoValue_Test extends $AutoValue_Test {\n" +
                "  AutoValue_Test(String str) {\n" +
                "    super(str);\n" +
                "  }\n" +
                "\n" +
                "  public Map<String, Object> toMap() {\n" +
                "    Map<String, Object> map = new HashMap<String, Object>();\n" +
                "    return map;\n" +
                "  }\n" +
                "\n" +
                "  public static Test fromMap(Map<String, Object> map) {\n" +
                "    String str;\n" +
                "    try {\n" +
                "      if (map.containsKey(\"str\")) {\n" +
                "        str = (String) map.get(\"str\");\n" +
                "      } else {\n" +
                "        NullMaker strMaker = (NullMaker) NullMaker.class.newInstance();\n" +
                "        str = (String) strMaker.make(\"str\");\n" +
                "      }\n" +
                "    } catch (InstantiationException | IllegalAccessException e) {\n" +
                "      throw new IllegalStateException(\"Cannot instantiate NullMaker for str because \"+e.getMessage());\n" +
                "    }\n" +
                "    return new AutoValue_Test(str);" +
                "  }\n" +
                "}"
        );

        JavaFileObject rootExpected = JavaFileObjects.forSourceString("com.raybritton.autovaluemap.$AutoValue_Test", "" +
                "package com.raybritton.autovaluemap;\n" +
                "\n" +
                "import com.raybritton.autovaluemap.annotations.MapHide;\n" +
                "import com.raybritton.autovaluemap.makers.NullMaker;\n" +
                "import javax.annotation.Generated;\n" +
                "\n" +
                "@Generated(\"com.google.auto.value.processor.AutoValueProcessor\")\n" +
                " abstract class $AutoValue_Test extends Test {\n" +
                "\n" +
                "  private final String str;\n" +
                "\n" +
                "  $AutoValue_Test(\n" +
                "      @Nullable String str) {\n" +
                "    this.str = str;\n" +
                "  }\n" +
                "\n" +
                "  @Nullable\n" +
                "  @MapHide(value = NullMaker.class, readFromMap = true)\n" +
                "  @Override\n" +
                "  public String str() {\n" +
                "    return str;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public String toString() {\n" +
                "    return \"Test{\"\n" +
                "        + \"str=\" + str\n" +
                "        + \"}\";\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public boolean equals(Object o) {\n" +
                "    if (o == this) {\n" +
                "      return true;\n" +
                "    }\n" +
                "    if (o instanceof Test) {\n" +
                "      Test that = (Test) o;\n" +
                "      return ((this.str == null) ? (that.str() == null) : this.str.equals(that.str()));\n" +
                "    }\n" +
                "    return false;\n" +
                "  }\n" +
                "\n" +
                "  @Override\n" +
                "  public int hashCode() {\n" +
                "    int h = 1;\n" +
                "    h *= 1000003;\n" +
                "    h ^= (str == null) ? 0 : this.str.hashCode();\n" +
                "    return h;\n" +
                "  }\n" +
                "\n" +
                "}\n"
        );

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor(Collections.singletonList(new AutoValueMapExtension())))
                .compilesWithoutError()
                .and()
                .generatesSources(expected, rootExpected);
    }
}
