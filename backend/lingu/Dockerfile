FROM python:3.12

ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

WORKDIR /app

COPY requirements.txt .

RUN pip install --upgrade pip
RUN pip install -r requirements.txt


COPY . .

COPY --from=dotnet /usr/share/dotnet /usr/share/dotnet
COPY --from=dotnet /usr/bin/dotnet /usr/bin/dotnet

ENV PATH="${PATH}:/usr/share/dotnet"


EXPOSE 8000