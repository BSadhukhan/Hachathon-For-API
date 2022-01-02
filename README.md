# Hachathon-For-API

# LMS RESTAPI
LMS API using 	Spring Boot

# Prerequisites/Development Environment
  Following software should be installed in the system.
  
    •	Java 
    
    •	SpringBoot 2.5.7
    
    •	Maven
    
    •	Postgres 14
    
    •	Eclipse IDE
    

# TO Run the App:
  Step 1: Download the Application and extract the Zip file.
  
  Step 2: Import the application to IDE.
  
  Step 3: Build Project. 
  
  Step 4: Check Tomcat port (8080).
  
  Step 5: Check Postgres DB connectivity, otherwise make changes the below configuration property in application.properties under src/main/resources.
  
          #PostGres Properties
          spring.datasource.url=jdbc:postgresql://localhost:5432/LMS_DB
          spring.datasource.username=postgres
          spring.datasource.password=admin
          

  Step 6: Then run the application.
  
  Step 7: Open the Postman / Swagger and test the application.
  

# Assumptions

  We have two sequences
  
      1.	user_seq for USER_ID of TBL_LMS_USER 
  
      Format – U{XX}, initialized with 01
  
      2.	user_skill_seq for USER_SKILL_ID of TBL_LMS_USERSKILL_MAP 
  
      Format – US{XX}, initialized with 01


***** Above these two customized sequences are using spring boot SequenceStyleGenerator.

***** It is advisable that the related tables should be empty, and the sequences values should be generated from 01. If some records are already present in the table, then execution will throw primary key violation constraint exception.

So in order to resolve the issue sequence need to be reset to the (max+1) value for that primary key column.

Below sqls need to be executed multiple times until respective sequences equal to the max value of that respective primary key column.

      1.	select nextval ('user_seq')

For example, let us assume that USER table has already 5 records and max value of USER_ID is ‘U05’. So, the above sql needs to be executed manually 5 times. So next time if your run the application spring boot will insert ‘U06’ value to USER_ID.

      2.	select nextval (' user_skill_seq ')

Same logic for this sequence also.

# API Documentation (Swagger)

•	http://localhost:8080/swagger-ui.html

# Modules & Endpoints

User API

  	GET – Fetch all user data from TBL_LMS_USER table.

      o	/Users 

  	GET – Fetch user data by USER_ID from TBL_LMS_USER table.

      o	/Users/{id}

  	POST – Insert a new User detail into TBL_LMS_USER table.

      o	/Users 
    
      o	Request Body
    
        {
          "name":"Baisali,Sadhukhan",        
          "phone_number":9123467545,        
          "location":"Pittsburgh",        
          "time_zone":"EST",        
          "linkedin_url":"www.linkedin.com/in/BaisaliSadhukhan",        
          "education_ug":"UG",        
          "education_pg":"PG",        
          "visa_status":"H4",        
          "comments":"Through Post"        
        }
      o	Response Body
        {
          "user_id": {Auto-Generated},
          "name":"Baisali,Sadhukhan",
          "phone_number":9123467545,
          "location":"Pittsburgh",
          "time_zone":"EST",
          "linkedin_url":"www.linkedin.com/in/BaisaliSadhukhan",
          "education_ug":"UG",
          "education_pg":"PG",
          "visa_status":"H4",
          "comments":"Through Post"
          "message_response": "Successfully Created !!"
        }

  	PUT – Update an existing User detail in TBL_LMS_USER table.

      o	/Users/{id}

      o	Request URL - /Users/U25
      o	Request Body
        {
            "user_id": "U25",
            "name": "Mounika,Badola",
            "phone_number": 9854658345,
            "location": "Pittsburgh",
            "time_zone": "EST",
            "linkedin_url": "www.linkedin.com/in",
            "education_ug": "UG",
            "education_pg": "PG",
            "visa_status": "H4",
            "comments": "Through Post"
        }
      o	Response Body
        {
            "user_id": "U25",
            "name": "Mounika,Badola",
            "phone_number": 9854658345,
            "location": "Pittsburgh",
            "time_zone": "EST",
            "linkedin_url": "https://www.linkedin.com/in/MounikaBadola/",
            "education_ug": "UG",
            "education_pg": "PG",
            "visa_status": "H4",
            "comments": "Through Post",
            "message_response": "Successfully Updated !!"
        }
        
  	DELETE – Delete an existing User detail from TBL_LMS_USER table.
    
    
       o	/Users/{id}
       o	Request URL - /Users/U05
       o	Response Body
        {
            "user_ id": "U05",
            "message_response": "The record has been deleted !!"
        }
        
Skill API

  	GET – Fetch all skill data from TBL_LMS_SKILL_MASTER table.
  
        o	/Skills
        
  	GET – Fetch skill data by SKILL_ID from TBL_LMS_USER table.
  
        o	/ Skills/{id}
        
  	POST – Insert a new Skill detail into TBL_LMS_USER table.
  
        o	/Skills
        
        o	Request Body
        
         {
            "skill_name": "Springg"
         }
         
        o	Response Body
         {
            "skill_id": 3,
            "skill_name": "Springg",
            "message_response": "Successfully Created !!"
         }
         
         
  	PUT – Update an existing Skill detail in TBL_LMS_USER table.
    
    
        o	/Skills/{id}
        o	Request URL - /Skills/3
        o	Request Body
          {
              "skill_id": 3,
              "skill_name": "Spring"
          }

        o	Response Body
          {
              "skill_id": 3,
              "skill_name": "Spring",
              "message_response": "Successfully Updated !!"
          }
          
          
  	DELETE – Delete an existing Skill detail from TBL_LMS_USER table.
    
    
        o	/Skills/{id}
        o	Request URL - /Skills/3
        o	Response Body
          {
              "message_response": "The record has been deleted !!",
              "Skill_Id": "3"
          }
         
UserSkillMap API

  	GET – Fetch all user-skill data from TBL_LMS_USERSKILL_MAP table.
  
        o	/UserSkills
        
  	GET – Fetch user-skill data by USER_SKILL_ID from TBL_LMS_USER table.
  
        o	/UserSkills/{id}
        
  	POST – Insert a new User-Skill mapping detail into TBL_LMS_USER table.
  
        o	/UserSkills 
        o	Request Body
          {
            "user_id": "U02",
            "skill_id": 2,
            "months_of_exp": 12
          }

        o	Response Body
          {
              "user_skill_id": "US55",
              "user_id": "U02",
              "skill_id": 2,
              "months_of_exp": 12,
              "message_response": "Successfully Created !!"
          }

  	PUT – Update an existing User-Skill mapping detail in TBL_LMS_USER table.
  
        o	/UserSkills/{id}
        o	Request URL - /UserSkills/US55
        o	Request Body
          {
            "user_skill_id": "US55",
            "user_id": "U02",
            "skill_id": 2,
            "months_of_exp": 18
          }

        o	Response Body
          {
              "user_skill_id": "US55",
              "user_id": "U02",
              "skill_id": 2,
              "months_of_exp": 18,
              "message_response": "Successfully Updated !!"
          }
          
  	DELETE – Delete an existing User-Skill mapping detail from TBL_LMS_USER table.
  
        o	/UserSkills/{id}
        o	Request URL - /UserSkills/US55
        o	Response Body
          {
              "user_skill_id": "US55",
              "message_response": "The record has been deleted !!"
          }
          
UserSkillMapGetAPI

  	GET – List all users with all skill details.
  
        o	/UserSkillsMap 
        
  	GET – List user with the skill details by USER_ID.
  
        o	/UserSkillsMap/{userId}
        o	Request URL – 
        o	Response Body
          {
              "users": {
                  "id": "U23",
                  "firstName": "Baisali",
                  "lastName": "Sadhukhan",
                  "skillmap": [
                      {
                          "id": 30,
                          "skill": "PostMan"
                      },
                      {
                          "id": 28,
                          "skill": "SpringBoot"
                      },
                      {
                          "id": 29,
                          "skill": "Java"
                      }
                  ]
              }
          }
          
  	GET  – List all users details by SKILL_ID.
  
        o	/UserSkillsMap/{skillId}
        
        
# Database Tables used:

1. TBL_LMS_USER
2. TBL_LMS_SKILL_MASTER
3. TBL_LMS_USERSKILL_MAP


