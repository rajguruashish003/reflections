package com.example.demo.controller;

import com.example.demo.model.APIStatus;
import com.example.demo.repo.APIStatusRepo;
import org.reflections.Reflections;
import org.springframework.security.access.prepost.PreAuthorize;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping(value = "/apiCheck")
public class APIDependencyCheck {

    @Autowired
    APIStatusRepo apiStatusRepo;

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> checkAPIs(@RequestHeader(value = "User-Agent", required = false) String userAgent) throws Exception {
        ResponseEntity<Object> responseEntity = null;
        Reflections reflections = new Reflections("com.example", new MethodAnnotationsScanner());

        apiStatusRepo.deleteAll();

        Set<Method> allRequestMappingMethods = reflections.getMethodsAnnotatedWith(RequestMapping.class);
//        Set<Method> allPreAuthorizeMethods = reflections.getMethodsAnnotatedWith(PreAuthorize.class);

        for (Method method : allRequestMappingMethods) {
            APIStatus status = new APIStatus();
            try {
                status.setClassName(method.getDeclaringClass().getName());
                status.setMethodName(method.getName());
                List<Annotation> annotations = Arrays.asList(method.getAnnotations());

                Annotation preAuthorizeAnnotation = annotations.stream().filter(annotation -> {
                    String name = annotation.annotationType().getName();
                    return name.toLowerCase().equalsIgnoreCase(PreAuthorize.class.getName().toLowerCase());
                }).findFirst().orElse(null);

                Annotation requestMapAnnotation = annotations.stream().filter(annotation -> {
                    String name = annotation.annotationType().getName();
                    return name.toLowerCase().equalsIgnoreCase(RequestMapping.class.getName().toLowerCase());
                }).findFirst().orElse(null);

                String requestMapMethodType = "";
                String requestMapAPI = "";
                if (requestMapAnnotation != null) {

                    Annotation requestMapAnnot = requestMapAnnotation;
                    if (requestMapAnnot != null) {
                        String requestMapPayload = requestMapAnnotation.toString();
                        status.setRequestMapPayload(requestMapPayload);
                        String requestMaps = requestMapPayload.replace("@org.springframework.web.bind.annotation.RequestMapping(", "").replace(")", "").replace("[", "").replace("]", "").replace("Optional[", "");
                        List<String> members = Arrays.asList(requestMaps.split(","));
                        String httpMethod = members.stream().filter(data -> {
                            return data.contains("method");
                        }).findFirst().get();

                        String apiPath = members.stream().filter(data -> {
                            return data.contains("value");
                        }).findFirst().get();

                        List<String> http = Arrays.asList(httpMethod.split("="));
                        if (http.size() > 1){
                            requestMapMethodType = http.get(1);
                        }else {
                            requestMapMethodType = "NO HTTP METHOD";
                        }


                        List<String> paths = Arrays.asList(apiPath.split("="));
                        if (paths.size() > 1) {
                            requestMapAPI = paths.get(1);
                        }else {
                            requestMapAPI = "NO API";
                        }
                    }
                }

                List<String> errors = new ArrayList<>();

                String preAuthorizeValue = "";
                String preAuthorizeAPI = "";
                String preAuthorizeAPIcheck =  "";
                String preAuthorizeMethod = "";
                if (preAuthorizeAnnotation != null) {
                    Annotation preAuthorAnnot = preAuthorizeAnnotation;
                    if (preAuthorAnnot != null) {
                        String preAuthorizePayload = preAuthorizeAnnotation.toString();
                        status.setPremissionPayload(preAuthorizePayload);

                        preAuthorizeValue = preAuthorizePayload.replace("@org.springframework.security.access.prepost.PreAuthorize(value=@authorizationcomp.hasPermission(authentication,", "").replace("(", "").replace(")", "").replace("'", "").replace("Optional[", "").replace("]", "");
                        List<String> elements = Arrays.asList(preAuthorizeValue.split(","));

                        if (elements.size() - 1 >= 1) {
                            preAuthorizeMethod = elements.get(1);
                        }else{
                            preAuthorizeMethod = "NO PERMISSION METHOD";
                        }
                        if (elements.size() - 1 >= 0) {
                            preAuthorizeAPI = elements.get(0);

                            List<String> paths = Arrays.asList(preAuthorizeAPI.split("/"));
                            if (paths.size() - 1 >= 0) {
                                preAuthorizeAPIcheck = paths.get(paths.size() - 1);
                            }else {
                                preAuthorizeAPIcheck = "NO PATH";
                            }
                        }else{
                            preAuthorizeAPI = "NO PERMISSION API";
                        }
                    }else {
                        errors.add("NO AUTHORIZATION");
                    }
                } else {
                    errors.add("NO AUTHORIZATION");
                }

                if (!preAuthorizeMethod.contains(requestMapMethodType) || !requestMapAPI.contains(preAuthorizeAPIcheck)) {
                    errors.add("API AND PERMISSION DOES NOT MATCH");
                }

                if (errors.size() > 0) {
                    status.setStatus(false);
                    status.setMessage(String.join(", ", errors));
                } else {
                    status.setStatus(true);
                    status.setMessage("SUCCESS");
                }

                status.setApiCode(requestMapAPI);
                status.setMethodTypeCode(requestMapMethodType);

                status.setApiDb(preAuthorizeAPI);
                status.setMethodTypeDb(preAuthorizeMethod);
                status.setId(UUID.randomUUID());
                status.setCreated(LocalDateTime.now());
                apiStatusRepo.save(status);
            }catch (Exception e){
                status.setMessage(e.getMessage());
                apiStatusRepo.save(status);
                e.printStackTrace();
            }
        }

        return  responseEntity;
    }
}

