package pl.rasztabiga.haldeserializer;

import pl.rasztabiga.haldeserializer.deserializer.Authentication;
import pl.rasztabiga.haldeserializer.deserializer.HalDeserializer;
import pl.rasztabiga.haldeserializer.deserializer.Resource;
import pl.rasztabiga.haldeserializer.entity.Account;
import pl.rasztabiga.haldeserializer.entity.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {

    private static Authentication.Header authenticationHeader;

    public static void main(String[] args) throws Exception {

        authenticationHeader = new Authentication.Header()
                .tokenAccessURL("https://infinite-future.eu.auth0.com/oauth/token")
                .clientID("5RF7Drx0eY8uh0uZ9e4C4a5L23Lbf83L")
                .clientSecret("PsA_YXFvCdmmjNIgKnH2fG0UmIXODmDtWSwQReoo3pFkFVW6FRReJ2vX92TMbwpk")
                .audience("https://api.klasa1a.pl")
                .grantType("client_credentials")
                .scope("read:exams read:news read:lucky-numbers read:surveys read:survey-options read:exams-photos read:students read:classes read:schools")
                .build();

        getSingleElement_example();
        getList_example();
        getOnDuties_example();

    }

    private static void getSingleElement_example() {
        HalDeserializer halDeserializer = new HalDeserializer.Builder()
                .baseUrl("http://api-v2.eu-central-1.elasticbeanstalk.com/accounts/1")
                .withAuthentication(authenticationHeader)
                .build();


        Resource<Account> accountRes = halDeserializer.toObject(Account.class);
        Account account = accountRes.getContent();
        System.out.println(account);
    }

    private static void getList_example() {
        HalDeserializer halDeserializer1 = new HalDeserializer.Builder()
                .baseUrl("http://api-v2.eu-central-1.elasticbeanstalk.com/accounts")
                .withAuthentication(authenticationHeader)
                .build();

        List<Resource<Account>> accountListRes = halDeserializer1.toList(Account.class);
        List<Account> accountList = accountListRes.stream()
                .map(Resource::getContent)
                .collect(Collectors.toList());
        System.out.println(accountList);
    }

    private static void getOnDuties_example() {
        Map<String, String> params = new HashMap<>();
        params.put("isOnDuty", "true");
        params.put("studentClass", "classes/1");

        HalDeserializer halDeserializer = new HalDeserializer.Builder()
                .baseUrl("http://api-v2.eu-central-1.elasticbeanstalk.com/students/search/onDuties")
                .withAuthentication(authenticationHeader)
                .withParams(params)
                .build();

        List<Resource<Student>> studentListRes = halDeserializer.toList(Student.class);
        List<Student> studentList = studentListRes.stream()
                .map(Resource::getContent)
                .collect(Collectors.toList());
        System.out.println(studentList);
    }
}
