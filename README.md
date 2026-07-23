To run python line coverage: 

 cd .\employee_app\   
Activate virtual environment
pip install pytest-cov  
pytest --cov=. --cov-report=html
Go to htmlcov folder and open index.html file in browser (easiest way to do this is is right click on it, reveal in file explorer, click on it in file explorer)

To run java line coverage:
cd .\manager_app\   
mvn test
mvn jacoco:report
Go to target/site/jacoco open index.html file in browser (easiest way to do this is is right click on it, reveal in file explorer, click on it in file explorer)


To run allure for unit tests:
Run Python tests using pytest in employee_app
run allure serve allure-results 
Run Java tests using mvn test in manager_app
run allure serve allure-results 

If you want the combined results:
In the project root, run .\combine-allure.ps1   
