# FileTextReader

A customized way to read Java/Kotlin text file with annotations in a simple and effective way

* **Download**
    https://github.com/AmadeAli1/FileTextReader/blob/master/FileTextReader.jar

* File Content (person.txt)

        firstname=Java|age=30|genre=Male|lastname=Kotlin
        firstname=John|lastname=Mclanne|age=12|genre=Male

* Example of a data class (model)

      @FileConfiguration(filename = "src\\person.txt", separator = "|")
      public class Person {
          @Field(min = 3)
          private String firstname;

        @Field(min = 2)
        private String lastname;

        @Field(type = DataType.BYTE)
        private Byte age;

        @Field(valid = {"Male", "Female"})
        private String genre;

        public Person(String firstname, String lastname, Byte age, String genre) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.age = age;
            this.genre = genre;
        }
        
        //Required Empty Constructor
        public Person() {
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
                    .add("firstname='" + firstname + "'")
                    .add("lastname='" + lastname + "'")
                    .add("age=" + age)
                    .add("genre='" + genre + "'")
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person person)) return false;

            if (!Objects.equals(firstname, person.firstname)) return false;
            if (!Objects.equals(lastname, person.lastname)) return false;
            return Objects.equals(age, person.age);
        }

        @Override
        public int hashCode() {
            int result = firstname != null ? firstname.hashCode() : 0;
            result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
            result = 31 * result + (age != null ? age.hashCode() : 0);
            return result;
        }
      }

* How to use

      public static void main(String [] args){
      
         FileTextReader<Pessoa> fileTextReader = new FileTextReaderImpl<>(Pessoa.class, new FileReaderListener<Pessoa>() {
                @Override
                public void onResult(Set<Pessoa> data) {
                    System.out.printf("Called:: %s%n", Instant.now());
                    data.forEach(System.out::println);
                }
            });

          fileTextReader.saveAll(new Person("Antony", "Tylor", (byte) 20, "Male"));
      }    

