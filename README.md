# Spring Boot Cyrela batch POC

Cyrela batch - Se propoe a automatizar o processo de ETL que faz parte do dia a dia da Cyrela.
Ele possui o potencial de se conectar a diversar fontes de dados heterogêneas e tratar estes dados, gerando bases muito mais limpas para serem utilizadas posteriormente na geração de relatórios.

## Getting Started:

O cyrela batch ainda encontra-se em fase de POC (proof of concept) por isso os dados refinados pelo processo batch estão sendo mantidos em arquivos .csv que encontra-se nos resources da aplicação!

## Run from Command Line:

Compile:
```
mnv clean install
```
run:
```
java -jar target/poc-etl-0.0.1-SNAPSHOT.jar
```

Após o comando acima o arquivo tratato para geração dos relatórios estará disponibilizado no diretório raiz 
