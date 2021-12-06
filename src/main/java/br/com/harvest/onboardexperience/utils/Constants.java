package br.com.harvest.onboardexperience.utils;

public class Constants {

    public static class Harvest {

        public static class User {
            public static final long ID = 1L;
            public static final String USERNAME = "${harvest.user.username}";
            public static final String EMAIL = "${harvest.user.email}";
            public static final String PASSWORD = "${harvest.user.password}";
            public static final String FIRST_NAME = "${harvest.user.first-name}";
            public static final String LAST_NAME = "${harvest.user.last-name}";
        }

        public static class Client {
            public static final long ID = 1L;
            public static final String CNPJ = "${harvest.client.cnpj}";
            public static final String NAME = "${harvest.client.name}";
            public static final String TENANT = "${harvest.client.tenant}";
            public static final String EMAIL = "${harvest.client.email}";
        }

        public static class CompanyRole {
            public static final String NAME = "${harvest.company-role.name}";
            public static final long ID = 1L;
        }

        public static class ScormCloud {
            public static final String SECRET_KEY = "${harvest.scorm-cloud.secret-key}";
            public static final String APP_ID = "${harvest.scorm-cloud.app-id}";
        }
    }
}
