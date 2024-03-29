package Gr8G1.prac.auth.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class UriUtil {
  public static URI createUri(String defaultUrl, long resourceId) {
    return UriComponentsBuilder.newInstance()
             .path(defaultUrl + "/{resource-id}")
             .buildAndExpand(resourceId)
             .toUri();
  }
}
