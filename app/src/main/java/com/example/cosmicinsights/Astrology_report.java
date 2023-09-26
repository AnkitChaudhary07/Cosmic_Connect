package com.example.cosmicinsights;

import java.io.Serializable;
import java.lang.String;
import java.util.List;

public class Astrology_report implements Serializable {
  private List<Item> item;

  private Auth auth;

  private List<Event> event;

  private Info info;

  public List<Item> getItem() {
    return this.item;
  }

  public void setItem(List<Item> item) {
    this.item = item;
  }

  public Auth getAuth() {
    return this.auth;
  }

  public void setAuth(Auth auth) {
    this.auth = auth;
  }

  public List<Event> getEvent() {
    return this.event;
  }

  public void setEvent(List<Event> event) {
    this.event = event;
  }

  public Info getInfo() {
    return this.info;
  }

  public void setInfo(Info info) {
    this.info = info;
  }

  public static class Item implements Serializable {
    private List<Item> item;

    private String name;

    public List<Item> getItem() {
      return this.item;
    }

    public void setItem(List<Item> item) {
      this.item = item;
    }

    public String getName() {
      return this.name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class Auth implements Serializable {
    private String type;

    private Basic basic;

    public String getType() {
      return this.type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public Basic getBasic() {
      return this.basic;
    }

    public void setBasic(Basic basic) {
      this.basic = basic;
    }

    public static class Basic implements Serializable {
      private String password;

      private String username;

      public String getPassword() {
        return this.password;
      }

      public void setPassword(String password) {
        this.password = password;
      }

      public String getUsername() {
        return this.username;
      }

      public void setUsername(String username) {
        this.username = username;
      }
    }
  }

  public static class Event implements Serializable {
    private String listen;

    private Script script;

    public String getListen() {
      return this.listen;
    }

    public void setListen(String listen) {
      this.listen = listen;
    }

    public Script getScript() {
      return this.script;
    }

    public void setScript(Script script) {
      this.script = script;
    }

    public static class Script implements Serializable {
      private String type;

      private List<String> exec;

      public String getType() {
        return this.type;
      }

      public void setType(String type) {
        this.type = type;
      }

      public List<String> getExec() {
        return this.exec;
      }

      public void setExec(List<String> exec) {
        this.exec = exec;
      }
    }
  }

  public static class Info implements Serializable {
    private String schema;

    private String name;

    private String _postman_id;

    public String getSchema() {
      return this.schema;
    }

    public void setSchema(String schema) {
      this.schema = schema;
    }

    public String getName() {
      return this.name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String get_postman_id() {
      return this._postman_id;
    }

    public void set_postman_id(String _postman_id) {
      this._postman_id = _postman_id;
    }
  }
}
