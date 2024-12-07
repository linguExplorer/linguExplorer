FROM python:3.12

ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

WORKDIR /app

COPY backend/lingu/requirements.txt .

RUN pip install --upgrade pip
RUN pip install -r requirements.txt

COPY backend/lingu/. .

EXPOSE 8000