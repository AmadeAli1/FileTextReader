# FileTextReader
A customized way to read Java/Kotlin text fileswith annotations in a simple and effective way

* Example of a data class (model)

      @FileConfiguration(filename = "src\\pessoa.txt", separator = "|")
      public class Pessoa {
          @Field(min = 3)
          private String firstname;

        @Field(min = 2)
        private String lastname;

        @Field(type = DataType.BYTE)
        private Byte idade;

        @Field(valid = {"solteiro", "casado", "playboy"})
        private String estado_civil;

        public Pessoa(String firstname, String lastname, Byte idade, String estado_civil) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.idade = idade;
            this.estado_civil = estado_civil;
        }

        public Pessoa() {
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Pessoa.class.getSimpleName() + "[", "]")
                    .add("firstname='" + firstname + "'")
                    .add("lastname='" + lastname + "'")
                    .add("idade=" + idade)
                    .add("estado_civil='" + estado_civil + "'")
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pessoa pessoa)) return false;

            if (!Objects.equals(firstname, pessoa.firstname)) return false;
            if (!Objects.equals(lastname, pessoa.lastname)) return false;
            return Objects.equals(idade, pessoa.idade);
        }

        @Override
        public int hashCode() {
            int result = firstname != null ? firstname.hashCode() : 0;
            result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
            result = 31 * result + (idade != null ? idade.hashCode() : 0);
            return result;
        }
      }
      
*   How to use
        
        public static void main(String [] args){
        
           FileTextReader<Pessoa> fileTextReader = new FileTextReaderImpl<>(Pessoa.class, new FileReaderListener<Pessoa>() {
                  @Override
                  public void onResult(Set<Pessoa> data) {
                      System.out.printf("Called:: %s%n", Instant.now());
                      data.forEach(System.out::println);
                  }
              });

            fileTextReader.saveAll(new Pessoa("Antony", "Tylor", (byte) 20, "playboy"));
        }    

