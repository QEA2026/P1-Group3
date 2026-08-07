FROM python:3.11-slim

WORKDIR /employee_app

# is this dangerous?
COPY . .

RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8080

CMD ["python", "app.py"]