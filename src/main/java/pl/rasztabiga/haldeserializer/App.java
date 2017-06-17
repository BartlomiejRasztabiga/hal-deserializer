package pl.rasztabiga.haldeserializer;

import pl.rasztabiga.haldeserializer.entity.Account;

import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {

        Authentication.Header authenticationHeader = new Authentication.Header()
                .tokenAccessURL("https://infinite-future.eu.auth0.com/oauth/token")
                .clientID("5RF7Drx0eY8uh0uZ9e4C4a5L23Lbf83L")
                .clientSecret("PsA_YXFvCdmmjNIgKnH2fG0UmIXODmDtWSwQReoo3pFkFVW6FRReJ2vX92TMbwpk")
                .audience("https://api.klasa1a.pl")
                .grantType("client_credentials")
                .scope("read:exams read:news read:lucky-numbers read:surveys read:survey-options read:exams-photos read:students read:classes read:schools")
                .build();

        HalDeserializer halDeserializer = new HalDeserializer.Builder()
                .baseUrl("http://api-v2.eu-central-1.elasticbeanstalk.com/accounts/1")
                .withAuthentication(authenticationHeader)
                .build();


        Account account = halDeserializer.toObject(Account.class);
        System.out.println(account);

        HalDeserializer halDeserializer1 = new HalDeserializer.Builder()
                .baseUrl("http://api-v2.eu-central-1.elasticbeanstalk.com/accounts")
                .withAuthentication(authenticationHeader)
                .build();

        List<Account> accountList = halDeserializer1.toList(Account.class);
        System.out.println(accountList);

    }
}
