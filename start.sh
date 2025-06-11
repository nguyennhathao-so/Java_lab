#!/bin/bash

# Start backend server
echo "Starting backend server..."
cd backend
mvn spring-boot:run &

# Wait for backend to start
sleep 10

# Start frontend server
echo "Starting frontend server..."
cd ../frontend
npx http-server . -p 8081 --cors -c-1